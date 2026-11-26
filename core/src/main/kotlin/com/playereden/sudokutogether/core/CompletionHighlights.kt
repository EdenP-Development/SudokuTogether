package com.playereden.sudokutogether.core

/**
 * A finish banner must only announce rewards genuinely unlocked by this
 * saved match record. Re-opening a completed puzzle may show the same earned
 * milestone, but never invents points or awards from an unsaved snapshot.
 */
data class CompletionHighlights(
    val achievements: List<String> = emptyList(),
    val cosmetics: List<String> = emptyList(),
    val nameWords: List<NameWord> = emptyList()
) {
    val hasNewRewards get() = achievements.isNotEmpty() || cosmetics.isNotEmpty() || nameWords.isNotEmpty()
}

object CompletionRewards {
    /** Compare durable ownership around a successful match save, not reconstructed history. */
    fun forSavedTransition(before: LocalPlayerData, after: LocalPlayerData, finishedId: String): CompletionHighlights {
        if (before.playerId != after.playerId || before.records.any { it.id == finishedId }) return CompletionHighlights()
        val finished = after.records.firstOrNull { it.id == finishedId } ?: return CompletionHighlights()
        val valid = when (finished.mode) {
            SOLO, DAILY, COOP -> finished.result == "SOLVED"
            VERSUS -> finished.result in setOf("WIN", "LOSS", "DRAW")
            else -> false
        }
        if (!valid) return CompletionHighlights()
        val priorIds = NameCollection.earnedIds(before)
        val newAchievements = Achievements.progress(after.records, after.earnedAchievementIds)
            .filter { it.unlocked && it.id !in priorIds }
        val ids = newAchievements.map { it.id }.toSet()
        val previous = Cosmetics.progressAll(before.records, before.ownedCosmeticIds)
        val current = Cosmetics.progressAll(after.records, after.ownedCosmeticIds)
        val catalog = Cosmetics.portraits + Cosmetics.emblems + Cosmetics.inks + Cosmetics.boards + Cosmetics.fonts
        return CompletionHighlights(
            newAchievements.map { it.title },
            catalog.filter { previous[it.id]?.unlocked == false && current[it.id]?.unlocked == true }.map { it.name },
            NameCollection.words.filter { it.achievementId in ids }
        )
    }

    fun forRecord(records: List<GameRecord>, finishedId: String): CompletionHighlights {
        val saved = records.distinctBy { it.id }
        val finished = saved.firstOrNull { it.id == finishedId && (it.result == "SOLVED" || it.result == "WIN" || (it.mode == VERSUS && it.result in setOf("LOSS", "DRAW"))) }
            ?: return CompletionHighlights()
        if (finished.mode !in setOf(SOLO, DAILY, COOP, VERSUS)) return CompletionHighlights()
        val before = saved.filterNot { it.id == finishedId }
        val priorAchievements = Achievements.progress(before).associateBy { it.id }
        val newlyReached = Achievements.progress(saved)
            .filter { it.unlocked && priorAchievements[it.id]?.unlocked == false }
        val newAchievements = newlyReached.map { it.title }
        val newIds = newlyReached.map { it.id }.toSet()
        val newWords = NameCollection.words.filter { it.achievementId != null && it.achievementId in newIds }
        val previous = Cosmetics.progressAll(before)
        val current = Cosmetics.progressAll(saved)
        val catalog = Cosmetics.portraits + Cosmetics.emblems + Cosmetics.inks +
            Cosmetics.boards + Cosmetics.fonts
        val newCosmetics = catalog.filter { item ->
            previous[item.id]?.unlocked == false && current[item.id]?.unlocked == true
        }.map { it.name }
        return CompletionHighlights(newAchievements, newCosmetics, newWords)
    }
}

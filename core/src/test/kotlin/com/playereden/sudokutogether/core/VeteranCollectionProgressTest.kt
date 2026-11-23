package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

/** Veteran goals must be worth earning without revoking old Beta 1/2 rewards. */
class VeteranCollectionProgressTest {
    private fun solved(
        id: String,
        mode: String = SOLO,
        difficulty: String? = "EASY",
        date: String? = null,
        assisted: Boolean = false,
        hints: Int = 0,
        checks: Int = 0
    ) = GameRecord(
        id, mode, "SOLVED", "p-$id", 900_000, 32, 1_000L,
        difficulty = difficulty, assisted = assisted, hintsUsed = hints,
        dailyDate = date, checksUsed = checks
    )

    private val veteran = linkedMapOf(
        "constellation" to 135,
        "sunrise" to 160,
        "focus" to 190,
        "compass" to 230,
        "moonlight" to 300,
        "grid_master" to 500
    )

    @Test fun original79ItemsAreRetainedAndSixNewEmblemsAreUnique() {
        val ids = Cosmetics.portraits + Cosmetics.emblems + Cosmetics.boards +
            Cosmetics.inks + Cosmetics.fonts
        assertEquals(43, Cosmetics.portraits.size)
        assertEquals(12, Cosmetics.emblems.size)
        assertEquals(85, ids.size)
        assertEquals(85, ids.map { it.id }.distinct().size)
        assertTrue(Cosmetics.emblems.map { it.id }.containsAll(listOf(
            "tile", "leaf", "duo", "flash", "star", "laurel"
        )))
        for ((id, _) in veteran) {
            assertFalse(Cosmetics.allowed(id, emptyList()))
            assertTrue(Cosmetics.emblems.any { it.id == id })
            assertTrue(Cosmetics.valid(Appearance(emblem = id)))
        }
    }

    @Test fun everyVeteranEmblemHasItsOwnCompletionThresholdAndPermanentOwnership() {
        // Co-op solves advance the general finish route, but none of the
        // optional Solo/clean/Daily goals; players are never forced to race.
        val all = (1..500).map { solved("coop-$it", mode = COOP) }
        for ((id, finish) in veteran) {
            assertEquals(finish, Cosmetics.progress(id, all)?.target)
            assertFalse("$id must not unlock early", Cosmetics.allowed(id, all.take(finish - 1)))
            assertTrue("$id must unlock at $finish", Cosmetics.allowed(id, all.take(finish)))
            val kept = Cosmetics.preserveEarned(LocalPlayerData(records = all.take(finish)))
                .copy(records = emptyList())
            assertTrue("$id should survive record pruning", id in kept.ownedCosmeticIds)
            assertEquals(id, Cosmetics.accessible(
                Appearance(emblem = id), kept.records, kept.ownedCosmeticIds
            ).emblem)
        }
    }

    @Test fun alternativeChallengeGoalsAreGenuineAndRespectAssistanceRules() {
        val hard = (1..50).map { solved("h-$it", difficulty = "HARD", assisted = true) }
        val medium = (1..40).map { solved("m-$it", difficulty = "MEDIUM", assisted = true) }
        val clean = (1..25).map { solved("c-$it") }
        val daily = (1..30).map { n ->
            // Date sequence valid and unique across November.
            solved("d-$n", mode = DAILY, date = "2026-11-" + n.toString().padStart(2, '0'))
        }
        val goals = listOf(
            Triple("constellation", hard, 15),
            Triple("sunrise", daily, 15),
            Triple("focus", clean, 25),
            Triple("compass", medium, 40),
            Triple("moonlight", daily, 30),
            Triple("grid_master", hard, 50)
        )
        for ((id, records, goal) in goals) {
            assertFalse("$id challenge must not unlock early", Cosmetics.allowed(id, records.take(goal - 1)))
            assertTrue("$id challenge grants emblem", Cosmetics.allowed(id, records.take(goal)))
            val progress = Cosmetics.progress(id, records.take(goal))!!
            assertEquals(goal, progress.alternateTarget)
            assertEquals(goal, progress.alternateCurrent)
        }
        val noisy = (1..25).map {
            solved("noisy-$it", assisted = true, hints = 1)
        }
        assertFalse("Hints should not count as clean solves", Cosmetics.allowed("focus", noisy))
        assertFalse("Medium solves must not count as Hard", Cosmetics.allowed("constellation", medium))
        assertFalse("Hard solves must not count as Medium", Cosmetics.allowed("compass", hard))
        val repeat = daily.take(14) + daily.take(14) +
            solved("extra-same-day", DAILY, date = "2026-11-14")
        assertEquals(14, Cosmetics.progress("sunrise", repeat)!!.alternateCurrent)
        assertFalse(Cosmetics.allowed("sunrise", repeat))
    }

    @Test fun thirtyOneAchievementsProvideLongTermSoloDailyDifficultyAndCleanGoals() {
        val medals = Achievements.progress(emptyList())
        assertEquals(31, medals.size)
        assertEquals(medals.size, medals.map { it.id }.toSet().size)
        assertTrue(medals.map { it.category }.toSet().containsAll(
            listOf("Solo", "Daily", "Difficulty", "Clean solves", "Together", "Versus")
        ))
        assertEquals(500, medals.first { it.id == "solo-500" }.target)
        assertEquals(60, medals.first { it.id == "daily-60" }.target)
        assertEquals(50, medals.first { it.id == "hard-50" }.target)
        assertEquals(50, medals.first { it.id == "clean-50" }.target)
        for (medal in medals) {
            medal.rewardId?.let { assertTrue("Reward $it must exist", it in Cosmetics.knownIds) }
        }
        assertEquals(0, Achievements.earnedCount(emptyList()))
    }

    @Test fun achievementMedalsEarnRetroactivelyAndCannotRelockAfterRecordsArePruned() {
        val hundred = (1..100).map { solved("solo-$it") }
        val earned = Achievements.preserveEarned(LocalPlayerData(records = hundred))
        assertTrue("solo-100" in earned.earnedAchievementIds)
        assertTrue("dedicated" in earned.earnedAchievementIds)
        assertTrue("clean-50" in earned.earnedAchievementIds)
        val old = earned.copy(records = emptyList())
        assertTrue(Achievements.progress(old.records, old.earnedAchievementIds)
            .first { it.id == "solo-100" }.unlocked)
        assertTrue(Achievements.progress(old.records, old.earnedAchievementIds)
            .first { it.id == "clean-50" }.unlocked)
        val second = Achievements.preserveEarned(old)
        assertEquals(old.earnedAchievementIds, second.earnedAchievementIds)
        assertFalse("Future medals should remain locked",
            Achievements.progress(old.records, old.earnedAchievementIds)
                .first { it.id == "solo-250" }.unlocked)
    }

    @Test fun duplicateAndMalformedDailyDatesDoNotMintExtraMedals() {
        val three = listOf(
            solved("a", DAILY, date = "2026-11-01"),
            solved("b", DAILY, date = "2026-11-02"),
            solved("c", DAILY, date = "2026-11-03")
        )
        val bad = three + three + solved("d", DAILY, date = "2026-11-03") +
            solved("e", DAILY, date = "invalid-date")
        val medal = Achievements.progress(bad).first { it.id == "daily-7" }
        assertEquals(3, medal.current)
        assertFalse(medal.unlocked)
        assertTrue(Achievements.progress(bad).first { it.id == "daily-3" }.unlocked)
    }
}

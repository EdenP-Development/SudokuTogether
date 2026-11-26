package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

class CompletionHighlightsTest {
    private fun solved(id: String, mode: String = SOLO, day: String? = null) =
        GameRecord(id, mode, "SOLVED", "puzzle-$id", 120_000L, 8, 150_000L, "EASY", dailyDate = day)

    @Test fun firstSoloFinishReportsOnlyRealNewMilestones() {
        val earned = CompletionRewards.forRecord(listOf(solved("first")), "first")
        assertTrue("First Grid" in earned.achievements)
        assertTrue("Rabbit" in earned.cosmetics)
        assertTrue("First Sprout" in earned.cosmetics)
        assertFalse("Owl is already available at the start", "Owl" in earned.cosmetics)
        assertFalse("Steady Hand" in earned.achievements)
    }

    @Test fun dailyCompletionUnlocksFinishMilestonesWithoutSoloAchievements() {
        val earned = CompletionRewards.forRecord(listOf(solved("today", DAILY, "2026-11-17")), "today")
        assertTrue("Rabbit" in earned.cosmetics)
        assertTrue(earned.achievements.isEmpty())
    }

    @Test fun duplicateRecordsAndAbsentFinishDoNotCreateFictitiousRewards() {
        val record = solved("one")
        assertEquals(CompletionRewards.forRecord(listOf(record), record.id),
            CompletionRewards.forRecord(listOf(record, record), record.id))
        assertFalse(CompletionRewards.forRecord(listOf(record), "not-saved").hasNewRewards)
        assertFalse(CompletionRewards.forRecord(listOf(
            record.copy(result = "IN_PROGRESS")), record.id).hasNewRewards)
    }

    @Test fun milestoneComparisonsAreRelativeToOtherSavedRecords() {
        val first = solved("one")
        val second = solved("two")
        val earned = CompletionRewards.forRecord(listOf(first, second), "two")
        assertFalse("First Grid" in earned.achievements)
        assertFalse("Rabbit" in earned.cosmetics)
        assertTrue("Linen" in earned.cosmetics)
    }

    private fun saved(profile: LocalPlayerData) =
        Achievements.preserveEarned(Cosmetics.preserveEarned(profile))

    @Test fun firstSavedTransitionAwardsExactlyTheNewAchievementWordBundles() {
        val before = LocalPlayerData()
        val record = solved("first").copy(elapsedMs = 650_000L)
        val after = saved(before.copy(records = listOf(record)))
        val rewards = CompletionRewards.forSavedTransition(before, after, record.id)
        assertEquals(listOf("First Grid"), rewards.achievements)
        assertEquals(setOf("First", "Acorn", "Solver"), rewards.nameWords.map { it.label }.toSet())
        assertEquals(3, rewards.nameWords.size)
        assertTrue(rewards.nameWords.all { it.achievementId == "first-grid" })
        assertTrue("Rabbit" in rewards.cosmetics)
    }

    @Test fun veteranLedgerPreventsReannouncingPrunedAchievementAndCosmeticRewards() {
        val before = LocalPlayerData(earnedAchievementIds = listOf("first-grid", "quick-thinker"),
            ownedCosmeticIds = Cosmetics.knownIds.toList())
        val record = solved("after-pruning")
        val after = saved(before.copy(records = listOf(record)))
        val rewards = CompletionRewards.forSavedTransition(before, after, record.id)
        assertFalse(rewards.hasNewRewards)
        assertTrue(NameCollection.earnedIds(after).containsAll(before.earnedAchievementIds))
        assertTrue(after.ownedCosmeticIds.containsAll(before.ownedCosmeticIds))
    }

    @Test fun duplicateMissingUnfinishedAndDifferentIdentityTransitionsAreSilent() {
        val before = LocalPlayerData()
        val record = solved("first")
        val after = saved(before.copy(records = listOf(record)))
        assertFalse(CompletionRewards.forSavedTransition(after, after, record.id).hasNewRewards)
        assertFalse(CompletionRewards.forSavedTransition(before, before, record.id).hasNewRewards)
        assertFalse(CompletionRewards.forSavedTransition(before, after, "missing").hasNewRewards)
        assertFalse(CompletionRewards.forSavedTransition(before, after.copy(playerId = "different"), record.id).hasNewRewards)
        assertFalse(CompletionRewards.forSavedTransition(before,
            before.copy(records = listOf(record.copy(result = "IN_PROGRESS"))), record.id).hasNewRewards)
    }

    @Test fun versusLossCanEarnParticipationWordsWithoutVictoryWords() {
        val before = LocalPlayerData()
        val record = solved("race", VERSUS).copy(result = "LOSS")
        val after = saved(before.copy(records = listOf(record)))
        val rewards = CompletionRewards.forSavedTransition(before, after, record.id)
        val expectedIds = NameCollection.earnedIds(after) - NameCollection.earnedIds(before)
        assertEquals(NameCollection.words.filter { it.achievementId in expectedIds }, rewards.nameWords)
        assertFalse(rewards.nameWords.any { it.achievementId == "challenger" })
        assertFalse("Challenger" in rewards.achievements)
    }

    @Test fun previouslyEarnedWordsStayAvailableWhileNextMilestoneIsAnnounced() {
        val before = saved(LocalPlayerData(records = (1..4).map { solved("solo-$it").copy(elapsedMs = 650_000L) }))
        val record = solved("solo-5").copy(elapsedMs = 650_000L)
        val after = saved(before.copy(records = before.records + record))
        val rewards = CompletionRewards.forSavedTransition(before, after, record.id)
        assertTrue(rewards.nameWords.any { it.achievementId == "steady-hand" })
        assertFalse(rewards.nameWords.any { it.achievementId == "first-grid" })
        assertTrue(NameCollection.earnedIds(after).containsAll(NameCollection.earnedIds(before)))
    }
}

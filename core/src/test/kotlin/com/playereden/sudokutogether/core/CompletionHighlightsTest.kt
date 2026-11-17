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
}

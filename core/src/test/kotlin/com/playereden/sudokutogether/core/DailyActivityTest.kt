package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

class DailyActivityTest {
    @Test fun recentHistoryUsesSavedDatesAcrossMonthAndExcludesRepeatedRecords() {
        fun completed(id: String, date: String) =
            GameRecord(id, DAILY, "SOLVED", "p", 1000L, 3, 2000L, "EASY", dailyDate = date)
        val puzzle = Puzzle("p", List(81) { 0 }, "EASY")
        val inProgress = DailyProgress("2026-11-17", PersonalPlay.newSolo(puzzle, 1000L))
        val records = listOf(
            completed("one", "2026-11-16"), completed("one", "2026-11-16"),
            completed("old", "2026-11-01"), completed("two", "2026-11-12"))
        val days = DailyActivity.recentSevenDays("2026-11-17", records, listOf(inProgress))
        assertEquals(7, days.size)
        assertEquals("2026-11-11", days.first().date)
        assertEquals("2026-11-17", days.last().date)
        assertEquals(2, days.count { it.complete })
        assertTrue(days.last().inProgress)
        assertFalse(days.last().complete)
        assertTrue(days.last().today)
        assertTrue(days[5].complete)
    }

    @Test fun aRetainedCompletedDayStillShowsAsCompleteWithoutARecentRecord() {
        val puzzle = Puzzle("p", List(81) { 0 }, "EASY")
        val finished = PersonalPlay.newSolo(puzzle, 1000L).copy(finishedAtMs = 2000L)
        val saved = DailyProgress("2026-11-17", finished)
        val day = DailyActivity.recentSevenDays("2026-11-17", emptyList(), listOf(saved)).last()
        assertTrue(day.complete)
        assertFalse(day.inProgress)
    }
}

package com.playereden.sudokutogether.core

import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

/** Read-only recent-day display; no streak penalties, bonuses or check-in rules. */
data class DailyActivityDay(
    val date: String,
    val weekday: String,
    val complete: Boolean,
    val inProgress: Boolean,
    val today: Boolean
)

object DailyActivity {
    fun recentSevenDays(today: String, records: List<GameRecord>, savedDays: List<DailyProgress>): List<DailyActivityDay> {
        val end = LocalDate.parse(today)
        val completed = records.asSequence()
            .filter { it.mode == DAILY && it.result == "SOLVED" && it.dailyDate != null }
            .mapNotNull { it.dailyDate }.toSet()
        val known = savedDays.associateBy { it.date }
        return (6L downTo 0L).map { offset ->
            val day = end.minusDays(offset)
            val date = day.toString()
            val saved = known[date]?.solo
            val done = date in completed || saved?.completed == true
            DailyActivityDay(
                date = date,
                weekday = day.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                complete = done,
                inProgress = !done && saved != null,
                today = offset == 0L
            )
        }
    }
}

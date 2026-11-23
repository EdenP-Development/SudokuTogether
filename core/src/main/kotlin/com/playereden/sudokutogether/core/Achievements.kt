package com.playereden.sudokutogether.core

import java.time.LocalDate

/**
 * Local/offline achievements. Keep all original IDs and thresholds, and credit
 * existing saved records retroactively. Earned IDs are stored separately so
 * older medals never disappear when the 10,000-record history is pruned.
 */
data class AchievementProgress(
    val id: String,
    val title: String,
    val description: String,
    val current: Int,
    val target: Int,
    val category: String = "Getting started",
    val rewardId: String? = null,
    val earned: Boolean = false
) {
    val unlocked: Boolean get() = earned || current >= target
}

object Achievements {
    fun progress(records: List<GameRecord>, earnedIds: Collection<String> = emptyList()): List<AchievementProgress> {
        // A reconnect or a repeated result callback must not mint extra progress.
        val completed = records.distinctBy { it.id }
        val solo = completed.filter { it.mode == SOLO && it.result == "SOLVED" }
        val coop = completed.count { it.mode == COOP && it.result == "SOLVED" }
        val wins = completed.count { it.mode == VERSUS && it.result == "WIN" }
        val races = completed.count { it.mode == VERSUS && it.result in listOf("WIN", "LOSS", "DRAW") }
        val daily = completed.filter { it.mode == DAILY && it.result == "SOLVED" }
            .mapNotNull { it.dailyDate }
            .filter { day -> runCatching { LocalDate.parse(day).toString() == day }.getOrDefault(false) }
            .toSet().size
        val medium = solo.count { it.difficulty?.uppercase() == "MEDIUM" }
        val hard = solo.count { it.difficulty?.uppercase() == "HARD" }
        val clean = solo.count { !it.assisted && it.hintsUsed == 0 && it.checksUsed == 0 }
        val fastest = solo.minOfOrNull { it.elapsedMs }
        val speed = if (fastest != null && fastest > 0 && fastest <= 10 * 60_000L) 1 else 0

        fun entry(id: String, title: String, description: String, current: Int, target: Int,
                  category: String, rewardId: String? = null) =
            AchievementProgress(id, title, description, current, target, category, rewardId, id in earnedIds)

        return listOf(
            // Original nine: never rename IDs or raise their requirements.
            entry("first-grid", "First Grid", "Complete your first solo Sudoku", solo.size, 1, "Solo"),
            entry("steady-hand", "Steady Hand", "Complete five solo puzzles", solo.size, 5, "Solo"),
            entry("dedicated", "Dedicated Solver", "Complete twenty solo puzzles", solo.size, 20, "Solo"),
            entry("solo-50", "Half-Century", "Solve fifty solo puzzles", solo.size, 50, "Solo"),
            entry("solo-100", "Century Club", "Solve 100 solo puzzles", solo.size, 100, "Solo"),
            entry("solo-250", "Puzzle Veteran", "Solve 250 solo puzzles", solo.size, 250, "Solo"),
            entry("solo-500", "Puzzle Legend", "Solve 500 solo puzzles", solo.size, 500, "Solo"),

            entry("daily-3", "Daily Visitor", "Complete three different Daily dates", daily, 3, "Daily"),
            entry("daily-7", "Seven Stamps", "Complete seven different Daily dates", daily, 7, "Daily"),
            entry("daily-15", "Sunrise Collector", "Complete 15 different Daily dates", daily, 15, "Daily", "sunrise"),
            entry("daily-30", "Moonlit Journal", "Complete 30 different Daily dates", daily, 30, "Daily", "moonlight"),
            entry("daily-60", "Calendar Keeper", "Complete 60 different Daily dates", daily, 60, "Daily"),

            entry("medium-10", "Finding Your Feet", "Solve ten Medium solo puzzles", medium, 10, "Difficulty"),
            entry("medium-25", "Steady Reasoning", "Solve 25 Medium solo puzzles", medium, 25, "Difficulty"),
            entry("medium-40", "True North", "Solve 40 Medium solo puzzles", medium, 40, "Difficulty", "compass"),
            entry("hard-5", "Tough Thinker", "Solve five Hard solo puzzles", hard, 5, "Difficulty"),
            entry("hard-15", "Constellation", "Solve 15 Hard solo puzzles", hard, 15, "Difficulty", "constellation"),
            entry("hard-50", "Master of the Grid", "Solve 50 Hard solo puzzles", hard, 50, "Difficulty", "grid_master"),

            entry("clean-5", "Clear Mind", "Solve five solo puzzles without hints or checks", clean, 5, "Clean solves"),
            entry("clean-12", "Sure Hand", "Solve 12 solo puzzles without hints or checks", clean, 12, "Clean solves"),
            entry("clean-25", "Focused Solver", "Solve 25 solo puzzles without hints or checks", clean, 25, "Clean solves", "focus"),
            entry("clean-50", "Unshaken", "Solve 50 solo puzzles without hints or checks", clean, 50, "Clean solves"),

            entry("two-minds", "Two Minds", "Finish a co-op board together", coop, 1, "Together"),
            entry("teamwork", "Teamwork", "Finish five co-op boards", coop, 5, "Together"),
            entry("team-15", "Puzzle Partners", "Finish 15 co-op boards together", coop, 15, "Together"),

            entry("challenger", "Challenger", "Win your first versus race", wins, 1, "Versus"),
            entry("rivalry", "Friendly Rivalry", "Play five versus races", races, 5, "Versus"),
            entry("champion", "Champion", "Win three versus races", wins, 3, "Versus"),
            entry("versus-15", "Regular Rival", "Play 15 versus races", races, 15, "Versus"),
            entry("versus-30", "Race Veteran", "Win 30 versus races", wins, 30, "Versus"),

            entry("quick-thinker", "Quick Thinker",
                "Solve a solo puzzle within ten minutes (assistance allowed)", speed, 1, "Solo")
        )
    }

    /** Stable allowlist for offline saves and portable backups. */
    val knownIds: Set<String> get() = progress(emptyList()).map { it.id }.toSet()

    fun preserveEarned(profile: LocalPlayerData): LocalPlayerData {
        val earned = (profile.earnedAchievementIds + progress(profile.records)
            .filter { it.unlocked }.map { it.id }).distinct().sorted()
        return if (earned == profile.earnedAchievementIds) profile
            else profile.copy(earnedAchievementIds = earned)
    }

    fun earnedCount(records: List<GameRecord>, earnedIds: Collection<String> = emptyList()): Int =
        progress(records, earnedIds).count { it.unlocked }
}

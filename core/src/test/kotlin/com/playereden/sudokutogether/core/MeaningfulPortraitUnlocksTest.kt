package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

/**
 * Owner playtest: meaningful earn routes while never revoking old Beta 1/2
 * completion-count rewards or requiring multiplayer for portrait cosmetics.
 */
class MeaningfulPortraitUnlocksTest {
    private fun solved(
        id: String, mode: String = SOLO, difficulty: String? = "EASY",
        day: String? = null, assisted: Boolean = false,
        hints: Int = 0, checks: Int = 0
    ) = GameRecord(
        id = id, mode = mode, result = "SOLVED", puzzleId = "p-$id",
        elapsedMs = 900_000L, moves = 35, finishedAtMs = 1000L,
        difficulty = difficulty, assisted = assisted, hintsUsed = hints,
        dailyDate = day, checksUsed = checks
    )

    @Test fun distinctDailyDatesUnlockGardenAndOceanRewardsWithoutLoginStreaks() {
        val dailies = (1..10).map { d ->
            solved("daily-$d", mode = DAILY, day = "2026-11-" + d.toString().padStart(2, '0'))
        }
        val steps = listOf(
            Triple("garden_strawberry", 2, "Daily"),
            Triple("ocean_seahorse", 3, "Daily"),
            Triple("farm_pumpkin", 4, "Daily"),
            Triple("garden_watering_can", 5, "Daily"),
            Triple("ocean_seashell", 7, "Daily"),
            Triple("garden_potted_tulip", 10, "Daily")
        )
        for ((id, target, word) in steps) {
            val before = dailies.take(target - 1)
            val earned = dailies.take(target)
            assertFalse("$id premature", Cosmetics.allowed(id, before))
            assertTrue("$id through different days", Cosmetics.allowed(id, earned))
            assertEquals(target, Cosmetics.progress(id, earned)!!.alternateTarget)
            assertEquals(target, Cosmetics.progress(id, earned)!!.alternateCurrent)
            assertTrue(Cosmetics.portraits.first { it.id == id }.requirement.contains(word))
        }
        // Repeated callbacks, duplicate match IDs and another puzzle on the
        // SAME date cannot mint another distinct Daily completion.
        val threeDates = dailies.take(3)
        val repeated = threeDates + threeDates + solved("different-id-same-day",
            DAILY, day = "2026-11-03")
        assertEquals(3, Cosmetics.progress("farm_pumpkin", repeated)!!.alternateCurrent)
        assertFalse(Cosmetics.allowed("farm_pumpkin", repeated))
        assertFalse(Cosmetics.allowed("garden_strawberry", listOf(
            solved("missing-date", DAILY),
            solved("bad-date", DAILY, day = "not-an-iso-date")
        )))
        assertEquals(4, Cosmetics.completedPuzzles(threeDates + listOf(
            solved("different-id-same-day", DAILY, day = "2026-11-03")
        )))
    }

    @Test fun mediumAndHardGoalsRewardChallengeWithoutNeedingVersus() {
        val medium = (1..7).map { solved("medium-$it", difficulty = "MEDIUM", assisted = true) }
        val hard = (1..5).map { solved("hard-$it", difficulty = "HARD", assisted = true) }
        val mediumGoals = listOf("farm_sheep" to 3, "woodland_beaver" to 5, "ocean_dolphin" to 7)
        val hardGoals = listOf("woodland_woodpecker" to 3, "farm_hay_bale" to 4, "ocean_clownfish" to 5)
        for ((id, threshold) in mediumGoals) {
            assertFalse(Cosmetics.allowed(id, medium.take(threshold - 1)))
            assertTrue(Cosmetics.allowed(id, medium.take(threshold)))
        }
        for ((id, threshold) in hardGoals) {
            assertFalse(Cosmetics.allowed(id, hard.take(threshold - 1)))
            assertTrue(Cosmetics.allowed(id, hard.take(threshold)))
        }
        // Medium does not masquerade as Hard; both accept coaching assistance
        // because difficulty, not hint avoidance, is this challenge.
        assertFalse(Cosmetics.allowed("woodland_woodpecker", medium))
        assertFalse(Cosmetics.allowed("ocean_dolphin", hard))
        assertEquals(7, Cosmetics.progress("ocean_dolphin", medium)!!.alternateCurrent)
    }

    @Test fun cleanSoloDoesNotCountHintCheckOrAssistedSolve() {
        val clean = (1..12).map { solved("clean-$it") }
        val noisy = listOf(
            solved("hinted", hints = 1),
            solved("checked", checks = 1),
            solved("assisted", assisted = true),
            solved("daily", DAILY, day = "2026-11-01")
        )
        for ((id, threshold) in listOf(
            "farm_pig" to 4,
            "garden_firefly" to 5,
            "woodland_skunk" to 12
        )) {
            assertFalse(Cosmetics.allowed(id, clean.take(threshold - 1) + noisy))
            assertTrue(Cosmetics.allowed(id, clean.take(threshold) + noisy))
            val progress = Cosmetics.progress(id, clean.take(threshold) + noisy)!!
            assertEquals(threshold, progress.alternateCurrent)
            assertTrue(Cosmetics.portraits.first { it.id == id }.requirement.contains("without hints"))
        }
        assertEquals(16, Cosmetics.completedPuzzles(clean + noisy))
    }

    @Test fun everyOldCompletionThresholdRemainsAValidPermanentUnlockRoute() {
        val oldTargets = linkedMapOf(
            "woodland_beaver" to 39, "woodland_woodpecker" to 56, "woodland_skunk" to 90,
            "ocean_seahorse" to 30, "ocean_dolphin" to 46, "ocean_seashell" to 67,
            "ocean_clownfish" to 95, "garden_strawberry" to 23,
            "garden_firefly" to 42, "garden_watering_can" to 62,
            "garden_potted_tulip" to 100, "farm_sheep" to 20, "farm_pig" to 36,
            "farm_pumpkin" to 58, "farm_hay_bale" to 85
        )
        // Co-op records do not qualify for the newly introduced Solo or Daily
        // challenges, but still advance original shared finish milestones.
        val records = (1..100).map { solved("coop-$it", mode = COOP, difficulty = null) }
        for ((id, oldTarget) in oldTargets) {
            assertFalse("$id old threshold should remain", Cosmetics.allowed(id, records.take(oldTarget - 1)))
            assertTrue("$id must retain original path", Cosmetics.allowed(id, records.take(oldTarget)))
            assertEquals(oldTarget, Cosmetics.progress(id, records.take(oldTarget))!!.target)
            val owned = Cosmetics.preserveEarned(LocalPlayerData(records = records.take(oldTarget)))
            assertTrue("$id ownership durable", id in owned.ownedCosmeticIds)
            val historyPruned = owned.copy(records = emptyList())
            assertTrue("$id owned after archive pruning",
                Cosmetics.allowed(id, historyPruned.records, historyPruned.ownedCosmeticIds))
        }
    }

    @Test fun alternateEarnedOwnershipSurvivesRecordPruningAndCatalogRemains79() {
        val daily = (1..3).map { solved("daily-$it", DAILY, day = "2026-11-0$it") }
        val earned = Cosmetics.preserveEarned(LocalPlayerData(records = daily))
        assertTrue("ocean_seahorse" in earned.ownedCosmeticIds)
        assertTrue("garden_strawberry" in earned.ownedCosmeticIds)
        val pruned = Cosmetics.preserveEarned(earned.copy(records = emptyList()))
        assertEquals(earned.ownedCosmeticIds, pruned.ownedCosmeticIds)
        assertTrue(Cosmetics.allowed("ocean_seahorse", pruned.records, pruned.ownedCosmeticIds))
        val ids = Cosmetics.portraits + Cosmetics.boards + Cosmetics.inks + Cosmetics.fonts + Cosmetics.emblems
        assertEquals(79, ids.size)
        assertEquals(79, ids.map { it.id }.toSet().size)
        assertEquals(setOf("woodland_owl", "woodland_fox", "ocean_octopus",
            "garden_honeybee", "farm_cow"),
            Cosmetics.portraits.filter { Cosmetics.allowed(it.id, emptyList()) }.map { it.id }.toSet())
        for (snapshot in listOf(emptyList(), daily, daily + daily)) {
            val bulk = Cosmetics.progressAll(snapshot)
            for (entry in ids) {
                assertEquals(entry.id, Cosmetics.progress(entry.id, snapshot), bulk[entry.id])
            }
        }
    }
}

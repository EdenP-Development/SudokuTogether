package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

/**
 * Owner playtest feedback: rewards should be accomplishments, not only a single
 * 100-completion counter. Alternative routes are additive; old finish-count
 * thresholds and all stable saved cosmetic IDs remain unchanged.
 */
class Beta2MixedUnlockPathsTest {
    private fun solo(id: String, difficulty: String = "EASY",
                     assisted: Boolean = false, hints: Int = 0, checks: Int = 0) =
        GameRecord(id, SOLO, "SOLVED", "grid-$id", 75_000L, 20, 100_000L,
            difficulty = difficulty, assisted = assisted, hintsUsed = hints, checksUsed = checks)

    private fun daily(id: String, date: String, result: String = "SOLVED") =
        GameRecord(id, DAILY, result, "grid-$id", 130_000L, 23, 200_000L,
            difficulty = "EASY", dailyDate = date)

    @Test fun firstDailyEarnsFarmChickenWithoutSevenEasyPuzzleGrind() {
        val item = Cosmetics.progress("farm_chicken", emptyList())!!
        assertEquals(0, item.current)
        assertEquals(1, item.target)
        assertEquals("Finish 1 different daily puzzle", item.pathLabel)
        assertFalse(item.unlocked)
        val completed = listOf(daily("day-1", "2026-11-22"))
        assertTrue(Cosmetics.allowed("farm_chicken", completed))
        assertEquals("Legacy fallback still works",
            true, Cosmetics.allowed("farm_chicken", (1..7).map { solo("old-$it") }))
        val earned = Cosmetics.preserveEarned(LocalPlayerData(records = completed))
        assertTrue("Daily-unlocked Farm Chicken must survive history retention",
            "farm_chicken" in earned.ownedCosmeticIds)
        val pruned = earned.copy(records = emptyList())
        assertTrue(Cosmetics.allowed("farm_chicken", pruned.records, pruned.ownedCosmeticIds))
    }

    @Test fun distinctDailyDatesCountOnceAndDoNotRewardAbortedGames() {
        val records = listOf(
            daily("d1", "2026-11-20"),
            daily("d1-replayed", "2026-11-20"),
            daily("d2", "2026-11-21"),
            daily("d3-failed", "2026-11-22", "ABANDONED"),
            daily("d4", "2026-11-22"),
            daily("invalid-date", "", "SOLVED"),
            daily("d5", "2026-11-23"),
            daily("d5", "2026-11-23")
        )
        assertEquals("Only distinct qualifying dates count", 4,
            Cosmetics.progress("woodland_chipmunk", records)!!.current.let {
                // Bonus is a 3-date path and is already unlocked after 3;
                // it displays its active path's count, not the finish total.
                it
            })
        assertTrue(Cosmetics.allowed("woodland_chipmunk", records))
        assertFalse("Five unique Dailies still needed for Seahorse",
            Cosmetics.allowed("ocean_seahorse", records))
        assertTrue(Cosmetics.portraits.first { it.id == "ocean_seahorse" }.requirement
            .contains("different daily puzzles"))
    }

    @Test fun difficultPuzzlesEarnAlternatesAndLossesDoNotCount() {
        val hard = (1..4).map { solo("hard-$it", difficulty = "HARD", assisted = it == 1) }
        val loss = GameRecord("loss", VERSUS, "LOSS", "p", 90000, 17, 800, difficulty = "HARD")
        assertTrue(Cosmetics.allowed("ocean_manta_ray", hard.take(3)))
        assertFalse(Cosmetics.allowed("woodland_woodpecker", hard.take(3)))
        assertTrue(Cosmetics.allowed("woodland_woodpecker", hard))
        assertFalse(Cosmetics.allowed("ocean_seashell", hard + loss))
        assertTrue(Cosmetics.allowed("ocean_seashell", hard + solo("hard-5", difficulty = "HARD")))
        assertTrue(Cosmetics.allowed("ocean_hermit_crab", hard))
        assertTrue(Cosmetics.progress("ocean_manta_ray", hard)!!.pathLabel!!.contains("Hard puzzle"))
    }

    @Test fun unassistedRouteRequiresActualUnassistedSoloRecords() {
        val clean = (1..8).map { solo("clean-$it") }
        val assisted = solo("hinted", assisted = true, hints = 1)
        val checked = solo("checked", checks = 1)
        assertTrue(Cosmetics.allowed("woodland_raccoon", clean.take(6)))
        assertTrue(Cosmetics.allowed("garden_strawberry", clean.take(4)))
        assertFalse(Cosmetics.allowed("farm_pig", clean.take(7) + assisted + checked))
        assertTrue(Cosmetics.allowed("farm_pig", clean))
    }

    @Test fun oldCompletionMilestonesAndExplicitlyOwnedCosmeticsNeverRelock() {
        val oldSolves = (1..100).map { solo("old-$it") }
        for (portrait in Cosmetics.portraits) {
            assertTrue("Existing 100-solve owner lost ${portrait.id}",
                Cosmetics.allowed(portrait.id, oldSolves))
        }
        val owned = Cosmetics.preserveEarned(LocalPlayerData(records = oldSolves))
        val pruned = owned.copy(records = emptyList())
        for (portrait in Cosmetics.portraits) {
            assertTrue("Retained reward lost ${portrait.id}",
                Cosmetics.allowed(portrait.id, pruned.records, pruned.ownedCosmeticIds))
        }
        assertEquals(43, Cosmetics.portraits.size)
        assertEquals(79, Cosmetics.knownIds.size)
    }

    @Test fun dailyAndDifficultyAlternativesAreCuratedAndClearlyDescribed() {
        val alternateIds = listOf(
            "woodland_chipmunk", "woodland_raccoon", "woodland_beaver",
            "woodland_woodpecker", "woodland_skunk",
            "ocean_hermit_crab", "ocean_seahorse", "ocean_manta_ray",
            "ocean_dolphin", "ocean_seashell", "ocean_clownfish",
            "garden_strawberry", "garden_firefly", "garden_potted_tulip",
            "farm_chicken", "farm_pig", "farm_hay_bale"
        )
        for (id in alternateIds) {
            val requirement = Cosmetics.portraits.single { it.id == id }.requirement
            assertTrue("$id must preserve the old completion milestone", requirement.startsWith("Complete "))
            assertTrue("$id must give a clearly labeled second route", requirement.contains(" or "))
        }
        assertEquals(17, alternateIds.size)
    }
}

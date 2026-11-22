package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

/** First-wave portraits: no old requirements changed, no paid or multiplayer dependency. */
class Beta2FirstFivePortraitsTest {
    private fun solo(index: Int) =
        GameRecord("solo-$index", SOLO, "SOLVED", "puzzle-$index", 1000, 1,
            index.toLong(), assisted = true)

    @Test fun firstWaveCatalogIsUniqueAndAllPortraitsAreRegistered() {
        val ids = Cosmetics.portraits.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
        val expected = setOf("farm_cow", "farm_chicken", "woodland_raccoon",
            "ocean_sea_turtle", "garden_firefly")
        assertTrue(ids.toSet().containsAll(expected))
        // Preserve first-five IDs while later Garden and Farm-themed packs are added.
        assertEquals(43, Cosmetics.portraits.size)
        assertEquals(6, Cosmetics.farmPortraits.size)
        for (id in expected) assertTrue(Cosmetics.valid(Appearance(portrait = id)))
        assertFalse(Cosmetics.valid(Appearance(portrait = "farm_unknown")))
    }

    @Test fun farmCowStarterAndHistoricalFinishThresholdsStillWorkForAllFive() {
        assertTrue(Cosmetics.allowed("farm_cow", emptyList()))
        val targets = mapOf(
            "farm_chicken" to 7,
            "ocean_sea_turtle" to 12,
            "woodland_raccoon" to 24,
            "garden_firefly" to 42,
        )
        for ((id, target) in targets) {
            assertFalse(Cosmetics.allowed(id, emptyList()))
            assertFalse(Cosmetics.allowed(id, (1 until target).map(::solo)))
            assertTrue(Cosmetics.allowed(id, (1..target).map(::solo)))
            assertEquals(target, Cosmetics.progress(id, (1..target).map(::solo))?.target)
        }
    }

    @Test fun oldCollectionRequirementsStayIdenticalAndNewOwnedIdsSurviveRecordPruning() {
        assertEquals("Complete 75 puzzles", Cosmetics.portraits.first { it.id == "ocean_narwhal" }.requirement)
        assertEquals("Complete 15 puzzles", Cosmetics.portraits.first { it.id == "woodland_cat" }.requirement)
        val earned = Cosmetics.preserveEarned(LocalPlayerData(records = (1..24).map(::solo)))
        assertTrue("woodland_raccoon" in earned.ownedCosmeticIds)
        assertTrue("farm_chicken" in earned.ownedCosmeticIds)
        val pruned = earned.copy(records = emptyList())
        assertTrue(Cosmetics.allowed("woodland_raccoon", pruned.records, pruned.ownedCosmeticIds))
        assertEquals(Appearance(portrait = "woodland_raccoon"),
            Cosmetics.accessible(Appearance(portrait = "woodland_raccoon"),
                pruned.records, pruned.ownedCosmeticIds))
    }

    @Test fun duplicateMatchIdsNeverMultiplyFarmMilestones() {
        val records = listOf(solo(1), solo(1), solo(2), solo(3),
            GameRecord("lost", VERSUS, "LOSS", "p", 1000, 1, 5))
        assertEquals(3, Cosmetics.completedPuzzles(records))
        assertFalse(Cosmetics.allowed("farm_chicken", records))
    }
}

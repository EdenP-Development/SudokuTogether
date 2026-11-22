package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

/** Beta 2 themed icons are still local/offline Player Icons, even when not animals. */
class Beta2GardenThemeIconsTest {
    private fun solo(i: Int) =
        GameRecord("garden-solo-$i", SOLO, "SOLVED", "garden-$i", 1000, 1, i.toLong())

    private val newGarden = linkedMapOf(
        "garden_sunflower" to 11,
        "garden_strawberry" to 23,
        "garden_watering_can" to 62,
        "garden_potted_tulip" to 100,
    )

    @Test fun gardenIncludesFlowersProduceAndItemWithoutChangingInsectMilestones() {
        assertEquals(9, Cosmetics.gardenPortraits.size)
        assertEquals(
            listOf("garden_ladybug", "garden_butterfly", "garden_sunflower"),
            Cosmetics.gardenPortraits.drop(1).take(3).map { it.id }
        ) // First locked row visibly includes a flower, not three insects.
        assertEquals(43, Cosmetics.portraits.size)
        val all = Cosmetics.portraits.map { it.id }
        assertEquals(all.size, all.toSet().size)
        assertTrue(all.containsAll(newGarden.keys))
        val original = mapOf(
            "garden_honeybee" to "Starter",
            "garden_ladybug" to "Complete 4 puzzles",
            "garden_butterfly" to "Complete 10 puzzles",
            "garden_dragonfly" to "Complete 20 puzzles",
            "garden_firefly" to "Complete 42 puzzles",
        )
        for ((id, expected) in original)
            assertEquals(expected, Cosmetics.gardenPortraits.first { it.id == id }.requirement)
        assertEquals(6, Cosmetics.emblems.size)
        assertEquals(11, Cosmetics.boards.size)
        assertEquals(10, Cosmetics.inks.size)
        assertEquals(9, Cosmetics.fonts.size)
        assertEquals(79, all.size + Cosmetics.emblems.size + Cosmetics.boards.size +
            Cosmetics.inks.size + Cosmetics.fonts.size)
    }

    @Test fun milestonesAreExactlyTheCatalogTargetsAndNeverRequireAnotherPlayer() {
        for ((id, threshold) in newGarden) {
            val prior = (1 until threshold).map(::solo)
            val earned = (1..threshold).map(::solo)
            assertEquals("Complete $threshold puzzles",
                Cosmetics.gardenPortraits.first { it.id == id }.requirement)
            assertFalse(Cosmetics.allowed(id, emptyList()))
            assertFalse(Cosmetics.allowed(id, prior))
            assertTrue(Cosmetics.allowed(id, earned))
            assertEquals(threshold, Cosmetics.progress(id, earned)?.target)
            assertTrue(Cosmetics.valid(Appearance(portrait = id)))
            assertEquals(Appearance(portrait = id),
                Cosmetics.accessible(Appearance(portrait = id), earned))
        }
    }

    @Test fun ownedFlowersSurviveRecordPruningAndUnknownRemoteIconsRemainBlocked() {
        val earned = Cosmetics.preserveEarned(LocalPlayerData(records = (1..100).map(::solo)))
        for (id in newGarden.keys) assertTrue(id in earned.ownedCosmeticIds)
        val pruned = earned.copy(records = emptyList())
        for (id in newGarden.keys) {
            assertTrue(Cosmetics.allowed(id, pruned.records, pruned.ownedCosmeticIds))
            assertEquals(id, Cosmetics.accessible(
                Appearance(portrait = id), pruned.records, pruned.ownedCosmeticIds).portrait)
        }
        assertFalse(Cosmetics.valid(Appearance(portrait = "garden_human")))
        assertEquals("", Cosmetics.accessible(
            Appearance(portrait = "garden_human"), pruned.records, pruned.ownedCosmeticIds).portrait)
    }

    @Test fun duplicateResultsAndLossesCannotGrantExtraGardenRewards() {
        val records = (1..10).map(::solo) +
            listOf(solo(1), solo(2), GameRecord("loss-g", VERSUS, "LOSS", "p", 1000, 1, 12))
        assertEquals(10, Cosmetics.completedPuzzles(records))
        assertFalse(Cosmetics.allowed("garden_sunflower", records))
        assertTrue(Cosmetics.allowed("garden_sunflower", records + solo(11)))
    }
}

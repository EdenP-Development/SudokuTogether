package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

/** Farm has animals, harvest crops and useful items; cosmetics remain local/offline. */
class Beta2FarmThemeIconsTest {
    private fun solo(i: Int) =
        GameRecord("farm-solo-$i", SOLO, "SOLVED", "farm-$i", 1000, 1, i.toLong(), assisted = true)

    private val newFarm = linkedMapOf(
        "farm_sheep" to 20,
        "farm_pig" to 36,
        "farm_pumpkin" to 58,
        "farm_hay_bale" to 85,
    )

    @Test fun farmHasSixIconsWithCropsAndObjectsWhileOldIdsAreUnchanged() {
        assertEquals(6, Cosmetics.farmPortraits.size)
        assertEquals(43, Cosmetics.portraits.size)
        val ids = Cosmetics.portraits.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
        assertTrue(ids.containsAll(newFarm.keys))
        assertEquals("Starter", Cosmetics.farmPortraits.first { it.id == "farm_cow" }.requirement)
        assertEquals("Complete 7 puzzles",
            Cosmetics.farmPortraits.first { it.id == "farm_chicken" }.requirement)
        assertEquals(85, ids.size + Cosmetics.emblems.size +
            Cosmetics.boards.size + Cosmetics.inks.size + Cosmetics.fonts.size)
        assertTrue(Cosmetics.allowed("farm_cow", emptyList()))
    }

    @Test fun farmOriginalFinishThresholdsRemainValidWithoutForcingMultiplayer() {
        for ((id, target) in newFarm) {
            val before = (1 until target).map(::solo)
            val after = (1..target).map(::solo)
            assertTrue(Cosmetics.farmPortraits.first { it.id == id }.requirement
                .startsWith("Complete $target puzzles"))
            assertFalse(Cosmetics.allowed(id, emptyList()))
            assertFalse(Cosmetics.allowed(id, before))
            assertTrue(Cosmetics.allowed(id, after))
            assertEquals(target, Cosmetics.progress(id, after)?.target)
            assertTrue(Cosmetics.valid(Appearance(portrait = id)))
            assertEquals(id, Cosmetics.accessible(
                Appearance(portrait = id), after).portrait)
        }
    }

    @Test fun farmOwnershipIsPermanentAfterRecordPruning() {
        val profile = Cosmetics.preserveEarned(
            LocalPlayerData(records = (1..85).map(::solo)))
        for (id in newFarm.keys) assertTrue(id in profile.ownedCosmeticIds)
        val pruned = profile.copy(records = emptyList())
        for (id in newFarm.keys) {
            assertTrue(Cosmetics.allowed(id, pruned.records, pruned.ownedCosmeticIds))
            assertEquals(id, Cosmetics.accessible(
                Appearance(portrait = id),
                pruned.records, pruned.ownedCosmeticIds).portrait)
        }
        assertFalse(Cosmetics.valid(Appearance(portrait = "farm_person")))
        assertEquals("", Cosmetics.accessible(
            Appearance(portrait = "farm_person"), pruned.records,
            pruned.ownedCosmeticIds).portrait)
    }

    @Test fun duplicateGameResultsAndLossesCannotGrantExtraFarmRewards() {
        val ten = (1..19).map(::solo)
        val records = ten + listOf(solo(1), solo(2),
            GameRecord("farm-loss", VERSUS, "LOSS", "p", 1000, 1, 28))
        assertEquals(19, Cosmetics.completedPuzzles(records))
        assertFalse(Cosmetics.allowed("farm_sheep", records))
        assertTrue(Cosmetics.allowed("farm_sheep", records + solo(20)))
    }
}

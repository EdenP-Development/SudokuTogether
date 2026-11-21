package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

/** Regression tests for 43-icon, 75-cosmetic unified beta reviewer. */
class Beta2Unified75CatalogTest {
    private fun solved(i: Int) = GameRecord("unified-$i", SOLO, "SOLVED", "puzzle-$i", 1000, 1, i.toLong())
    private val newWoodland = linkedMapOf("woodland_chipmunk" to 16, "woodland_beaver" to 39,
        "woodland_woodpecker" to 56, "woodland_skunk" to 90)
    private val newOcean = linkedMapOf("ocean_seahorse" to 30, "ocean_dolphin" to 46,
        "ocean_seashell" to 67, "ocean_clownfish" to 95)

    @Test fun allFourIconFamiliesAndFullCatalogHaveUniqueStableIds() {
        assertEquals(17, Cosmetics.woodlandPortraits.size)
        assertEquals(11, Cosmetics.oceanPortraits.size)
        assertEquals(9, Cosmetics.gardenPortraits.size)
        assertEquals(6, Cosmetics.farmPortraits.size)
        assertEquals(43, Cosmetics.portraits.size)
        val all = Cosmetics.portraits + Cosmetics.emblems + Cosmetics.boards +
            Cosmetics.inks + Cosmetics.fonts
        assertEquals(75, all.size)
        assertEquals(75, all.map { it.id }.toSet().size)
        assertEquals(11, Cosmetics.boards.size)
        assertEquals(10, Cosmetics.inks.size)
        assertEquals(5, Cosmetics.fonts.size)
        assertEquals(6, Cosmetics.emblems.size)
        assertTrue(all.map { it.id }.containsAll(newWoodland.keys + newOcean.keys))
        assertEquals("Complete 24 puzzles", Cosmetics.woodlandPortraits.first {
            it.id == "woodland_raccoon" }.requirement)
    }

    @Test fun allEightMilestonesRequireQualifiedCompletedPuzzlesOnly() {
        for ((id, target) in newWoodland + newOcean) {
            val prior = (1 until target).map(::solved)
            val earned = (1..target).map(::solved)
            assertEquals("Complete $target puzzles", Cosmetics.portraits.first { it.id == id }.requirement)
            assertFalse(Cosmetics.allowed(id, prior))
            assertTrue(Cosmetics.allowed(id, earned))
            assertEquals(target, Cosmetics.progress(id, earned)?.target)
            assertTrue(Cosmetics.valid(Appearance(portrait = id)))
            assertEquals(id, Cosmetics.accessible(Appearance(portrait = id), earned).portrait)
        }
    }

    @Test fun collectedIconsStayOwnedWhenOldRecordsArePruned() {
        val earned = Cosmetics.preserveEarned(LocalPlayerData(records = (1..95).map(::solved)))
        val pruned = earned.copy(records = emptyList())
        for (id in newWoodland.keys + newOcean.keys) {
            assertTrue(id in pruned.ownedCosmeticIds)
            assertTrue(Cosmetics.allowed(id, pruned.records, pruned.ownedCosmeticIds))
            assertEquals(id, Cosmetics.accessible(Appearance(portrait = id),
                emptyList(), pruned.ownedCosmeticIds).portrait)
        }
    }

    @Test fun duplicatesLossesAndUnknownRemoteIdsRemainSafe() {
        val prior = (1..15).map(::solved)
        val noisy = prior + solved(1) + GameRecord("lost-u75", VERSUS, "LOSS", "p", 1000, 1, 11L)
        assertEquals(15, Cosmetics.completedPuzzles(noisy))
        assertFalse(Cosmetics.allowed("woodland_chipmunk", noisy))
        assertTrue(Cosmetics.allowed("woodland_chipmunk", noisy + solved(16)))
        assertFalse(Cosmetics.valid(Appearance(portrait = "unknown_new_cosmetic")))
        assertEquals("", Cosmetics.accessible(Appearance(portrait = "unknown_new_cosmetic"), noisy).portrait)
    }
}

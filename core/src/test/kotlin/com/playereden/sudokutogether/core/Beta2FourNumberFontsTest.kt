package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

/** Beta 2 full 79-cosmetic catalog and durable font entitlement contracts. */
class Beta2FourNumberFontsTest {
    private fun solved(i: Int) =
        GameRecord("font-$i", SOLO, "SOLVED", "puzzle-$i", 1000, 1, i.toLong())

    private val additions = linkedMapOf(
        "typewriter" to 14, "editorial" to 26,
        "soft_print" to 45, "tall_print" to 75
    )

    @Test fun nineDistinctFontIdsPreserveOriginalFiveAndReach79Cosmetics() {
        assertEquals(listOf("clear", "serif", "mono", "rounded", "pencil",
            "typewriter", "editorial", "soft_print", "tall_print"),
            Cosmetics.fonts.map { it.id })
        val catalog = Cosmetics.portraits + Cosmetics.emblems + Cosmetics.boards +
            Cosmetics.inks + Cosmetics.fonts
        assertEquals(85, catalog.size)
        assertEquals(85, catalog.map { it.id }.toSet().size)
        assertEquals("Starter", Cosmetics.fonts.first { it.id == "clear" }.requirement)
        assertEquals("Complete 2 co-op puzzles",
            Cosmetics.fonts.first { it.id == "mono" }.requirement)
        assertEquals("Complete 8 puzzles",
            Cosmetics.fonts.first { it.id == "pencil" }.requirement)
    }

    @Test fun allFourFontsUnlockAtTheDisplayedFinishMilestone() {
        for ((id, target) in additions) {
            val before = (1 until target).map(::solved)
            val after = (1..target).map(::solved)
            assertEquals("Complete $target puzzles",
                Cosmetics.fonts.first { it.id == id }.requirement)
            assertFalse(Cosmetics.allowed(id, before))
            assertTrue(Cosmetics.allowed(id, after))
            assertEquals(target, Cosmetics.progress(id, after)?.target)
            assertTrue(Cosmetics.valid(Appearance(font = id)))
            assertEquals(id, Cosmetics.accessible(Appearance(font = id), after).font)
        }
    }

    @Test fun alreadyEarnedFontsSurviveRecordCleanupAndUnknownFontFallsBack() {
        val earned = Cosmetics.preserveEarned(LocalPlayerData(records = (1..75).map(::solved)))
        val pruned = earned.copy(records = emptyList())
        for (id in additions.keys) {
            assertTrue(id in pruned.ownedCosmeticIds)
            assertTrue(Cosmetics.allowed(id, emptyList(), pruned.ownedCosmeticIds))
            assertEquals(id, Cosmetics.accessible(Appearance(font = id),
                emptyList(), pruned.ownedCosmeticIds).font)
        }
        assertFalse(Cosmetics.valid(Appearance(font = "unknown_font_data")))
        assertEquals("clear", Cosmetics.accessible(
            Appearance(font = "unknown_font_data"), emptyList()).font)
    }

    @Test fun duplicatedRecordOrLossNeverAdvancesFontUnlocks() {
        val first = (1..13).map(::solved)
        val invalid = first + solved(1) +
            GameRecord("font-loss", VERSUS, "LOSS", "p", 1000, 1, 15L)
        assertEquals(13, Cosmetics.completedPuzzles(invalid))
        assertFalse(Cosmetics.allowed("typewriter", invalid))
        assertTrue(Cosmetics.allowed("typewriter", invalid + solved(14)))
    }
}

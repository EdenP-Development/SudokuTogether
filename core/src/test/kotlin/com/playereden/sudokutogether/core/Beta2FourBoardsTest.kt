package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

/** Four new visual-only Beta 2 board themes; legacy entitlements never change. */
class Beta2FourBoardsTest {
    private fun solo(i: Int) = GameRecord("board-$i", SOLO, "SOLVED", "puzzle-$i", 1000, 1, i.toLong())
    private val additions = linkedMapOf("moss" to 16, "rosewood" to 28, "pebble" to 48, "tidal" to 70)

    @Test fun boardCatalogHasStableLegacyOrderAndFourNewOptions() {
        val ids = Cosmetics.boards.map { it.id }
        assertEquals(listOf("classic", "linen", "midnight", "circuit",
            "parchment", "ocean", "graphite", "moss", "rosewood", "pebble", "tidal"), ids)
        assertEquals(75, Cosmetics.portraits.size + Cosmetics.emblems.size +
            Cosmetics.boards.size + Cosmetics.inks.size + Cosmetics.fonts.size)
        assertEquals("Starter", Cosmetics.boards.first { it.id == "classic" }.requirement)
        assertEquals("Solve 2 solo puzzles",
            Cosmetics.boards.first { it.id == "linen" }.requirement)
        assertEquals("Win 3 versus races",
            Cosmetics.boards.first { it.id == "circuit" }.requirement)
    }

    @Test fun milestonesAreSoloAccessibleAndMatchDisplayLabels() {
        for ((id, target) in additions) {
            val before = (1 until target).map(::solo)
            val after = (1..target).map(::solo)
            assertEquals("Complete $target puzzles",
                Cosmetics.boards.first { it.id == id }.requirement)
            assertFalse(Cosmetics.allowed(id, before))
            assertTrue(Cosmetics.allowed(id, after))
            assertEquals(target, Cosmetics.progress(id, after)?.target)
            assertTrue(Cosmetics.valid(Appearance(board = id)))
            assertEquals(id, Cosmetics.accessible(Appearance(board = id), after).board)
        }
    }

    @Test fun permanentOwnershipSurvivesPruningAndUnknownIdsFallBack() {
        val earned = Cosmetics.preserveEarned(LocalPlayerData(records = (1..70).map(::solo)))
        assertTrue(earned.ownedCosmeticIds.containsAll(additions.keys))
        val pruned = earned.copy(records = emptyList())
        for (id in additions.keys) {
            assertTrue(Cosmetics.allowed(id, emptyList(), pruned.ownedCosmeticIds))
            assertEquals(id, Cosmetics.accessible(Appearance(board = id),
                emptyList(), pruned.ownedCosmeticIds).board)
        }
        assertFalse(Cosmetics.valid(Appearance(board = "unregistered_palette")))
        assertEquals("classic", Cosmetics.accessible(
            Appearance(board = "unregistered_palette"), emptyList()).board)
    }

    @Test fun duplicateAndLostGamesDoNotEarnRewards() {
        val prior = (1..15).map(::solo)
        val noisy = prior + solo(1) + GameRecord("lost", VERSUS, "LOSS", "p", 1000, 1, 9L)
        assertEquals(15, Cosmetics.completedPuzzles(noisy))
        assertFalse(Cosmetics.allowed("moss", noisy))
        assertTrue(Cosmetics.allowed("moss", noisy + solo(16)))
    }

    @Test fun sharedBoardOrderAddsNewThemesWithoutAlteringLegacyMatches() {
        val old = listOf("classic", "linen", "midnight", "circuit",
            "parchment", "ocean", "graphite")
        for ((i, host) in old.withIndex())
            for ((j, guest) in old.withIndex())
                assertEquals(old[maxOf(i, j)], Cosmetics.sharedCoopBoard(host, guest))
        assertEquals("tidal", Cosmetics.sharedCoopBoard("linen", "tidal"))
        assertEquals("classic", Cosmetics.sharedCoopBoard("unknown", "unregistered"))
    }
}

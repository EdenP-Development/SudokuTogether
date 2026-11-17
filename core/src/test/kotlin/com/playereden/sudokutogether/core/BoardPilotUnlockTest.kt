package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

/** New board unlocks must stay offline, predictable, and safe for older saves. */
class BoardPilotUnlockTest {
    private fun wins(n: Int): List<GameRecord> =
        List(n) { index -> GameRecord("solo-$index", SOLO, "SOLVED", "p", 1000L, 1, 1000L) }

    @Test fun parchmentAndGraphiteRequireSoloReachableMilestones() {
        assertEquals("Solve 8 solo puzzles", Cosmetics.boards.first { it.id == "parchment" }.requirement)
        assertEquals("Complete 20 puzzles", Cosmetics.boards.first { it.id == "graphite" }.requirement)

        assertFalse(Cosmetics.allowed("parchment", wins(7)))
        assertTrue(Cosmetics.allowed("parchment", wins(8)))
        assertFalse(Cosmetics.allowed("graphite", wins(19)))
        assertTrue(Cosmetics.allowed("graphite", wins(20)))
        assertEquals(CosmeticProgress(7, 8), Cosmetics.progress("parchment", wins(7)))
        assertEquals(CosmeticProgress(19, 20), Cosmetics.progress("graphite", wins(19)))

        val duplicated = wins(7) + wins(7) // identical game IDs must never unlock a board
        assertFalse(Cosmetics.allowed("parchment", duplicated))
        assertFalse(Cosmetics.allowed("graphite", duplicated))
        assertFalse(Cosmetics.allowed("parchment", List(8) {
            GameRecord("loss-$it", VERSUS, "LOSS", "p", 1000L, 1, 1000L)
        }))
    }

    @Test fun newAppearanceIdsAreValidButLockedOnFreshProfiles() {
        val starter = emptyList<GameRecord>()
        for (board in listOf("parchment", "graphite")) {
            val appearance = Appearance(board = board)
            assertTrue("Board must be allowlisted for saves and same-version LAN", Cosmetics.valid(appearance))
            assertEquals("Locked boards must not be equipped from a fresh profile",
                "classic", Cosmetics.accessible(appearance, starter).board)
            assertEquals(board, Cosmetics.accessible(appearance, wins(20)).board)
        }
        assertTrue(Cosmetics.valid(Appearance()))
        assertEquals("classic", Cosmetics.accessible(Appearance(board = "classic"), starter).board)
        assertFalse(Cosmetics.valid(Appearance(board = "never-registered")))
    }

    @Test fun oldBoardPrioritiesAreUnchangedAndNewBoardsParticipate() {
        val old = listOf("classic", "linen", "midnight", "circuit")
        for ((i, host) in old.withIndex()) {
            for ((j, guest) in old.withIndex()) {
                assertEquals(old[maxOf(i, j)], Cosmetics.sharedCoopBoard(host, guest))
            }
        }
        assertEquals("parchment", Cosmetics.sharedCoopBoard("parchment", "classic"))
        assertEquals("graphite", Cosmetics.sharedCoopBoard("linen", "graphite"))
        assertEquals("graphite", Cosmetics.sharedCoopBoard("parchment", "graphite"))
    }
}

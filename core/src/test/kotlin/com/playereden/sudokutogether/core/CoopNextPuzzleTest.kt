package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

class CoopNextPuzzleTest {
    @Test fun staleOrCancelledNextPuzzleCannotReplaceTheCurrentRound() {
        val game = GameSession.create(fixture())
        game.admitGuest("Happy Fox")
        HostServer(game.also { it.prepareCanonicalNames("Happy Bear") }).use { host ->
            val before = game.snapshot()
            val next = fixture().copy(id = "next-coop")
            assertFalse(host.nextCoopPuzzle(before.epoch, next) { false })
            assertEquals(before, game.snapshot())
            assertTrue(host.nextCoopPuzzle(before.epoch, next))
            val after = game.snapshot()
            assertEquals(before.sessionId, after.sessionId)
            assertNotEquals(before.epoch, after.epoch)
            assertEquals(before.guestName, after.guestName)
            assertEquals(before.hostAppearance, after.hostAppearance)
            assertEquals(before.guestAppearance, after.guestAppearance)
            assertEquals(next.clues.map(::Cell), after.cells)
            assertFalse(host.nextCoopPuzzle(before.epoch, fixture()))
            assertEquals(after, game.snapshot())
        }
    }
}

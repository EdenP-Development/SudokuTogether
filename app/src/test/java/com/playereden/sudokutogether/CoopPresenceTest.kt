package com.playereden.sudokutogether

import com.playereden.sudokutogether.core.*
import org.junit.Assert.*
import org.junit.Test

class CoopPresenceTest {
    private val puzzle = Puzzle("presence", List(81) { 0 }, "EASY")
    private val snapshot = Snapshot("room", "epoch", puzzle, List(81) { Cell() },
        hostName = "Host", guestName = "Partner")
    private val base = GameUiState(page = Page.PLAY, role = HOST, snapshot = snapshot,
        connected = true, status = RoomStatus(guestActive = true, reserved = true))

    @Test fun openSeatAndReservedSeatAreDifferent() {
        assertEquals("Waiting for your partner to join", coopPresence(base.copy(
            snapshot = snapshot.copy(guestName = null), status = RoomStatus())).label)
        val paused = coopPresence(base.copy(status = RoomStatus(reserved = true)))
        assertTrue(paused.attention)
        assertFalse(paused.reconnect)
        assertEquals("Paused while your partner is away", paused.label)
    }

    @Test fun guestCanReconnectEvenAfterCompletion() {
        val guest = base.copy(role = GUEST, connected = false)
        assertTrue(coopPresence(guest).reconnect)
        assertTrue(coopPresence(guest.copy(snapshot = snapshot.copy(completed = true))).reconnect)
        assertFalse(coopPresence(base.copy(snapshot = snapshot.copy(completed = true))).reconnect)
    }

    @Test fun activeAndHostAwayStatesAreDistinct() {
        assertEquals("Solving with Partner", coopPresence(base).label)
        assertEquals("Paused while the host is away", coopPresence(base.copy(role = GUEST,
            status = RoomStatus(hostActive = false, guestActive = true, reserved = true))).label)
    }
}

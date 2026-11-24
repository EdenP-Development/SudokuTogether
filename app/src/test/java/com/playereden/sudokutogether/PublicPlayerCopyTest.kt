package com.playereden.sudokutogether

import org.junit.Assert.*
import org.junit.Test

class PublicPlayerCopyTest {
    @Test fun historyUsesNaturalLabels() {
        assertEquals("Solo", historyModeLabel("solo"))
        assertEquals("Daily", historyModeLabel("daily"))
        assertEquals("Co-op", historyModeLabel("coop"))
        assertEquals("Versus", historyModeLabel("versus"))
        assertEquals(listOf("Solved","Won","Lost","Draw","Finished"),
            listOf("SOLVED","WIN","LOSS","DRAW","unexpected").map(::historyResultLabel))
    }
    @Test fun joinErrorsKeepUsefulRecoveryAndSuppressUnknownText() {
        assertEquals("Incorrect room code. Try again.", nearbyJoinFailureMessage("That room code is incorrect."))
        assertEquals("This room already has two players.", nearbyJoinFailureMessage("This room already has two players."))
        assertEquals("Too many incorrect codes. Try again in a minute.", nearbyJoinFailureMessage("Too many incorrect codes. Try again in a minute."))
        assertTrue(nearbyJoinFailureMessage("The host is running an incompatible app version.").contains("same app version"))
        assertEquals("Couldn't reach the host. Check Wi-Fi and try again.", nearbyJoinFailureMessage("java.net.SocketException: /192.168.0.1"))
        assertEquals(nearbyJoinFailureMessage(null), nearbyJoinFailureMessage("arbitrary peer text"))
    }
}

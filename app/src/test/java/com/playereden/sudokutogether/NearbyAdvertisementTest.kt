package com.playereden.sudokutogether

import com.playereden.sudokutogether.core.WIRE_PROTOCOL_VERSION
import org.junit.Assert.*
import org.junit.Test

class NearbyAdvertisementTest {
    private val session = "12345678-abcd-4321-9876-123456a7f291"
    private fun attributes(flag: String? = null, version: Int = WIRE_PROTOCOL_VERSION) =
        buildMap { put("v",version.toString().toByteArray());put("session",session.toByteArray());if (flag != null) put("locked",flag.toByteArray()) }
    @Test fun missingAndUnrecognizedAccessFlagsStayProtected() {
        for (flag in listOf(null,"","false","1","unknown"))
            assertTrue(decodeNearbyRoom("Taylor's game","192.168.1.20",41421,attributes(flag))!!.requiresCode)
        assertFalse(decodeNearbyRoom("Taylor's game","192.168.1.20",41421,attributes("0"))!!.requiresCode)
    }
    @Test fun incompatibleVersionsAndMissingEndpointsAreNotOffered() {
        assertNull(decodeNearbyRoom("Old game","192.168.1.20",41421,attributes("0",6)))
        assertNull(decodeNearbyRoom("No address",null,41421,attributes("0")))
        assertNull(decodeNearbyRoom("No port","192.168.1.20",0,attributes("0")))
    }
    @Test fun validDiscoveryDerivesLabelAndRetainsAddressAndRejoinSession() {
        val room=decodeNearbyRoom("Taylor's game","fe80::2",41421,attributes("0"))!!
        assertEquals("GridMates puzzle A7F291",room.name);assertEquals("fe80::2",room.host)
        assertEquals(41421,room.port);assertEquals(session,room.sessionId);assertFalse(room.requiresCode)
    }

    @Test fun remoteNamesNeverReachTheDisplayedLabel() {
        for (name in listOf("", "Personal information", "Unexpected\nmessage", "X".repeat(300))) {
            val room = decodeNearbyRoom(name, "192.168.1.20", 41421, attributes())!!
            assertEquals("GridMates puzzle A7F291", room.name)
        }
    }
    @Test fun malformedAndMissingSessionIdsAreRejected() {
        for (id in listOf("", "saved-game", "123456", session + "x", session.replace("a7f291", "a7f29g"))) {
            assertNull(decodeNearbyRoom("Room", "192.168.1.20", 41421, attributes() + ("session" to id.toByteArray())))
        }
        assertNull(decodeNearbyRoom("Room", "192.168.1.20", 41421, attributes() - "session"))
    }
}

package com.playereden.sudokutogether.core

import java.io.IOException
import org.junit.Assert.*
import org.junit.Test

class RejoinIdentityTest {
    @Test fun differentRoomWelcomeIsRejectedBeforeAnyClientEvent() {
        val session = GameSession.create(fixture())
        HostServer(session.also { it.prepareCanonicalNames("Happy Bear") }).use { host ->
            val port = host.start()
            val events = mutableListOf<ClientEvent>()
            GuestClient(listener = { events.add(it) }).use { guest ->
                val error = assertThrows(IOException::class.java) {
                    guest.connect("127.0.0.1", port, host.code, "Friendly Bear",
                        expectedSessionId = "unrelated-room")
                }
                assertTrue(error.message.orEmpty().contains("different puzzle room"))
                assertTrue(events.isEmpty())
            }
        }
    }

    @Test fun expectedRoomAllowsWelcome() {
        val session = GameSession.create(fixture())
        HostServer(session.also { it.prepareCanonicalNames("Happy Bear") }).use { host ->
            val port = host.start()
            val events = mutableListOf<ClientEvent>()
            GuestClient(listener = { events.add(it) }).use { guest ->
                guest.connect("127.0.0.1", port, host.code, "Friendly Bear",
                    expectedSessionId = session.snapshot().sessionId)
                assertEquals(session.snapshot().sessionId, events.first().snapshot?.sessionId)
            }
        }
    }
}

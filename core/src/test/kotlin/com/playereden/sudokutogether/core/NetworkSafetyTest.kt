package com.playereden.sudokutogether.core

import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import org.junit.Assert.*
import org.junit.Test

class NetworkSafetyTest {
    private fun state(): Snapshot = GameSession.create(fixture(), hostName = "Happy Bear").also {
        it.admitCanonicalGuest("Lucky Otter")
    }.snapshotForGuest()

    @Test fun admissionPersistenceFailureDoesNotLeakInternalDetails() {
        var fail = false
        val game = GameSession.create(fixture(), hostName = "Happy Bear", requireCode = false,
            persist = { if (fail) throw IOException("Private diagnostic detail") })
        HostServer(game).use { host ->
            val port = host.start()
            fail = true
            Socket("127.0.0.1", port).use { socket ->
                socket.soTimeout = 3000
                Protocol.write(socket.getOutputStream(), Message("hello", name = "Lucky Otter"))
                val reply = Protocol.read(socket.getInputStream())
                assertEquals("error", reply.type)
                assertEquals("Could not join this room.", reply.text)
                assertNull(reply.token)
                assertNull(game.saved().guest)
            }
        }
    }

    @Test fun guestRejectsUnapprovedWelcomeNamesBeforePublishingState() {
        val valid = state()
        for (bad in listOf(valid.copy(hostName = "Unknown Name"), valid.copy(guestName = "Happy  Fox"))) {
            ServerSocket(0).use { server ->
                val peer = thread(isDaemon = true) {
                    server.accept().use { socket ->
                        socket.soTimeout = 3000
                        Protocol.read(socket.getInputStream())
                        Protocol.write(socket.getOutputStream(), Message("welcome", snapshot = bad,
                            status = RoomStatus(true, true, true), token = "x".repeat(64)))
                    }
                }
                val events = LinkedBlockingQueue<ClientEvent>()
                GuestClient(listener = events::offer).use { guest ->
                    assertThrows(IOException::class.java) {
                        guest.connect("127.0.0.1", server.localPort, "", "Lucky Otter")
                    }
                    assertTrue(events.isEmpty())
                }
                peer.join(3000)
            }
        }
    }

    @Test fun unexpectedRemoteErrorsAreNotDisplayedVerbatim() {
        assertEquals("Could not join this room.", approvedRoomError("Private diagnostic detail"))
        assertEquals("That room code is incorrect.", approvedRoomError("That room code is incorrect."))
        ServerSocket(0).use { server ->
            val peer = thread(isDaemon = true) {
                server.accept().use { socket ->
                    socket.soTimeout = 3000
                    Protocol.read(socket.getInputStream())
                    Protocol.write(socket.getOutputStream(), Message("error", text = "Untrusted remote message"))
                }
            }
            GuestClient().use { guest ->
                val error = assertThrows(IOException::class.java) {
                    guest.connect("127.0.0.1", server.localPort, "", "Lucky Otter")
                }
                assertEquals("Could not join this room.", error.message)
            }
            peer.join(3000)
        }
    }
}

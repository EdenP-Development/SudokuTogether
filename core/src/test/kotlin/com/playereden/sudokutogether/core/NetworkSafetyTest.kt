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

    @Test fun liveStatesRejectInvalidNamesBoardsAndInconsistentOutcomes() {
        val valid = state()
        val status = RoomStatus(true, true, true)
        val good = Outcome(true, ResultCode.ACCEPTED, valid, "operation")
        val invalidMessages = listOf(
            Message("state", snapshot = valid.copy(hostName = "Unknown Name"), status = status),
            Message("state", snapshot = valid.copy(cells = emptyList()), status = status),
            Message("state", snapshot = valid, status = status, outcome = good.copy(snapshot = valid.copy(guestName = "Unknown Name"))),
            Message("state", snapshot = valid, status = status, outcome = good.copy(snapshot = valid.copy(cells = emptyList()))),
            Message("state", snapshot = valid, status = status, outcome = good.copy(snapshot = valid.copy(epoch = "different"))),
            Message("state", snapshot = valid, status = status, outcome = good.copy(snapshot = valid.copy(hostName = "Clever Fox")))
        )
        for (invalid in invalidMessages) {
            ServerSocket(0).use { server ->
                val release = java.util.concurrent.CountDownLatch(1)
                val peer = thread(isDaemon = true) {
                    server.accept().use { socket ->
                        socket.soTimeout = 3000
                        Protocol.read(socket.getInputStream())
                        Protocol.write(socket.getOutputStream(), Message("welcome", snapshot = valid,
                            status = status, token = "x".repeat(64)))
                        Protocol.write(socket.getOutputStream(), Message("state", snapshot = valid, status = status, outcome = good))
                        Protocol.write(socket.getOutputStream(), invalid)
                        release.await(8, TimeUnit.SECONDS)
                    }
                }
                val events = LinkedBlockingQueue<ClientEvent>()
                try {
                    GuestClient(listener = events::offer).use { guest ->
                        guest.connect("127.0.0.1", server.localPort, "", "Lucky Otter")
                        assertEquals(valid, events.poll(5, TimeUnit.SECONDS)?.snapshot)
                        assertEquals(good, events.poll(5, TimeUnit.SECONDS)?.outcome)
                        val rejection = events.poll(5, TimeUnit.SECONDS)
                        assertNotNull("Invalid live state must disconnect", rejection)
                        assertTrue(rejection!!.disconnected)
                        assertNull(rejection.snapshot)
                        assertNull(rejection.outcome)
                    }
                } finally {
                    release.countDown()
                    peer.join(3000)
                }
            }
        }
    }
}

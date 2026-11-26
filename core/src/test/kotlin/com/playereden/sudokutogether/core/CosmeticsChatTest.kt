package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class CosmeticsChatTest {
    @Test fun unlocksAreEarnedFromActualCompletedRecordsAndUnknownIdsAreRejected() {
        val at = 1234L
        val solved = GameRecord("s", SOLO, "SOLVED", "p", 45000, 20, at)
        val win = GameRecord("w", VERSUS, "WIN", "p", 45000, 20, at)
        assertFalse(Cosmetics.allowed("cobalt", emptyList()))
        assertTrue(Cosmetics.allowed("forest", emptyList()))
        assertFalse(Cosmetics.allowed("invalid", emptyList()))
        assertTrue(Cosmetics.allowed("cobalt", listOf(solved)))
        assertFalse(Cosmetics.allowed("violet", listOf(solved, win)))
        assertTrue(Cosmetics.allowed("violet", listOf(solved, win, win.copy(id = "w2"))))
        assertEquals(Appearance(ink = "forest"), Cosmetics.accessible(Appearance(ink = "violet"),
            listOf(solved)))
    }

    @Test fun playerDigitsRetainTheirOwnerAndUndoRestoresEarlierAuthoredStyle() {
        val game = session()
        val a = Appearance("cobalt", "linen", "serif")
        val b = Appearance("rose", "classic", "clear")
        game.setAppearance(HOST, a)
        game.setAppearance(GUEST, b)
        assertTrue(game.submit(HOST, game.snapshot().move(0, Kind.NOTE, 2)).accepted)
        assertEquals(HOST, game.snapshot().cells[0].owner)
        assertTrue(game.submit(GUEST, game.snapshot().move(0, number = 1)).accepted)
        assertEquals(GUEST, game.snapshot().cells[0].owner)
        assertEquals(b, game.snapshotForGuest().guestAppearance)
        assertEquals(a, game.snapshotForGuest().hostAppearance)
        assertTrue(game.submit(GUEST, game.snapshot().move(0, Kind.UNDO)).accepted)
        assertEquals(HOST, game.snapshot().cells[0].owner)
        assertEquals(2, game.snapshot().cells[0].notes)
        assertNull(game.snapshot().cells[2].owner)
    }

    @Test fun messagesAppearOnBothPhonesButOnlyAuthenticatedPeerChoosesSender() {
        val hostEvents = LinkedBlockingQueue<HostEvent>()
        val guestEvents = LinkedBlockingQueue<ClientEvent>()
        HostServer(GameSession.create(fixture()), hostEvents::offer).use { host ->
            val port = host.start()
            GuestClient(listener = guestEvents::offer).use { guest ->
                guest.connect("127.0.0.1", port, host.code, "Rival",
                    appearance = Appearance(ink = "cobalt"))
                await(guestEvents) { it.token != null }
                assertTrue(host.chat("  Good game!  "))
                assertFalse("Immediate repeat should be rate-limited", host.chat("Another message too soon"))
                assertEquals("Good game!", await(guestEvents) { it.chat?.player == HOST }.chat!!.text)
                guest.sendChat("Nice move!")
                val received = await(hostEvents) { it.chat?.player == GUEST }.chat!!
                assertEquals("Nice move!", received.text)
                assertEquals(GUEST, await(guestEvents) { it.chat?.player == GUEST }.chat!!.player)
            }
        }
    }

    @Test fun chatCleansControlsAndBoundsText() {
        assertEquals("hello world", ChatRules.clean(" \nhello\u0000 world\r "))
        assertEquals(ChatRules.MAX_LENGTH, ChatRules.clean("x".repeat(300)).length)
        assertEquals("", ChatRules.clean("\u0000  "))
    }

    @Test fun explicitHostBindingAcceptsOnlyTheSelectedLoopbackAddress() {
        val address = java.net.InetAddress.getByName("127.0.0.1")
        HostServer(GameSession.create(fixture())).use { host ->
            val port = host.start(address)
            assertEquals(port, host.start(address))
            assertThrows(IllegalStateException::class.java) {
                host.start(java.net.InetAddress.getByName("127.0.0.2"))
            }
            val events = LinkedBlockingQueue<ClientEvent>()
            GuestClient(listener = events::offer).use { guest ->
                guest.connect("127.0.0.1", port, host.code, "Rival")
                assertNotNull(await(events) { it.token != null }.token)
            }
            assertThrows(java.io.IOException::class.java) {
                java.net.Socket().use { it.connect(java.net.InetSocketAddress("127.0.0.2", port), 1000) }
            }
        }
    }

    @Test fun replacingPartnerClearsChatButReconnectingPreservesIt() {
        val hostEvents = LinkedBlockingQueue<HostEvent>()
        HostServer(GameSession.create(fixture()), hostEvents::offer).use { host ->
            val port = host.start(java.net.InetAddress.getByName("127.0.0.1"))
            val first = LinkedBlockingQueue<ClientEvent>()
            val token = GuestClient(listener = first::offer).use { guest ->
                guest.connect("127.0.0.1", port, host.code, "First")
                val credential = await(first) { it.token != null }.token!!
                assertTrue(host.chat("Previous conversation"))
                await(first) { it.chat?.text == "Previous conversation" }
                credential
            }
            await(hostEvents) { !it.status.guestActive && it.snapshot.guestName != null }
            val returning = LinkedBlockingQueue<ClientEvent>()
            GuestClient(listener = returning::offer).use { guest ->
                guest.connect("127.0.0.1", port, host.code, "First", resumeToken = token)
                assertTrue(await(returning) { it.token != null }.history.orEmpty().any { it.text == "Previous conversation" })
                host.replaceGuest()
            }
            val replacement = LinkedBlockingQueue<ClientEvent>()
            GuestClient(listener = replacement::offer).use { guest ->
                guest.connect("127.0.0.1", port, host.code, "Second")
                assertEquals(emptyList<ChatLine>(), await(replacement) { it.token != null }.history)
            }
        }
    }

    private fun <T> await(queue: LinkedBlockingQueue<T>, match: (T) -> Boolean): T {
        val deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(8)
        while (System.nanoTime() < deadline) {
            val item = queue.poll(100, TimeUnit.MILLISECONDS)
            if (item != null && match(item)) return item
        }
        throw AssertionError("Missing expected LAN chat event")
    }
}

package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class PartnerPingTest {
    private fun marker(snapshot: Snapshot, cell: Int = 4) = PartnerPing(snapshot.sessionId, snapshot.epoch, cell)

    @Test fun markerRequiresValidCurrentUnfinishedCoopRound() {
        val snapshot = GameSession.create(fixture()).snapshot()
        val ping = marker(snapshot)
        assertTrue(ping.matches(snapshot))
        assertFalse(ping.copy(cell = -1).matches(snapshot))
        assertFalse(ping.copy(cell = 81).matches(snapshot))
        assertFalse(ping.copy(sessionId = "other").matches(snapshot))
        assertFalse(ping.copy(epoch = "old").matches(snapshot))
        assertFalse(ping.matches(snapshot.copy(mode = VERSUS)))
        assertFalse(ping.matches(snapshot.copy(completed = true)))
    }

    @Test fun markerFramesRejectMalformedOrMisplacedPayloads() {
        fun read(message: Message): Message {
            val bytes = ByteArrayOutputStream()
            Protocol.write(bytes, message)
            return Protocol.read(ByteArrayInputStream(bytes.toByteArray()))
        }
        val ping = marker(GameSession.create(fixture()).snapshot())
        assertEquals(ping, read(Message("partner_ping", partnerPing = ping)).partnerPing)
        for (bad in listOf(ping.copy(cell = -1), ping.copy(cell = 81), ping.copy(epoch = ""), ping.copy(sessionId = "x".repeat(129)))) {
            assertThrows(IOException::class.java) { read(Message("partner_ping", partnerPing = bad)) }
        }
        assertThrows(IOException::class.java) { read(Message("partner_ping")) }
        assertThrows(IOException::class.java) { read(Message("welcome", partnerPing = ping)) }
    }

    @Test fun activePartnersCanPingBothWaysWithoutChangingPuzzleOrHistory() {
        val hostEvents = LinkedBlockingQueue<HostEvent>()
        val guestEvents = LinkedBlockingQueue<ClientEvent>()
        HostServer(GameSession.create(fixture(), hostName = "Happy Bear"), hostEvents::offer).use { host ->
            val port = host.start()
            assertFalse(host.partnerPing(marker(host.session.snapshot())))
            GuestClient(listener = guestEvents::offer).use { guest ->
                guest.connect("127.0.0.1", port, host.code, "Happy Fox")
                await(guestEvents) { it.token != null }
                val before = host.session.snapshot()
                val ping = marker(before)
                assertFalse(host.partnerPing(ping.copy(epoch = "stale")))
                assertFalse(guest.partnerPing(ping.copy(sessionId = "wrong")))
                assertTrue(host.partnerPing(ping))
                assertFalse(host.partnerPing(ping.copy(cell = 5)))
                assertEquals(ping, await(guestEvents) { it.partnerPing != null }.partnerPing)
                val reply = ping.copy(cell = 8)
                assertTrue(guest.partnerPing(reply))
                val received = await(hostEvents) { it.partnerPing != null }
                assertEquals(reply, received.partnerPing)
                assertNull(received.chat)
                assertEquals(before, host.session.snapshot())
                host.setForeground(false)
                await(guestEvents) { it.status?.hostActive == false }
                assertFalse(host.partnerPing(ping))
                assertFalse(guest.partnerPing(reply))
                host.setForeground(true)
                await(guestEvents) { it.status?.hostActive == true }
                host.newPuzzle(12345L)
                val current = await(guestEvents) { it.snapshot?.epoch != null && it.snapshot.epoch != ping.epoch }.snapshot!!
                assertFalse(host.partnerPing(ping))
                assertFalse(guest.partnerPing(reply))
                assertTrue(host.partnerPing(marker(current)))
                assertEquals(marker(current), await(guestEvents) { it.partnerPing != null }.partnerPing)
            }
        }
    }

    @Test fun versusRejectsMarkersFromBothPlayers() {
        val events = LinkedBlockingQueue<ClientEvent>()
        HostServer(GameSession.create(fixture(), hostName = "Happy Bear", mode = VERSUS)).use { host ->
            val port = host.start()
            GuestClient(listener = events::offer).use { guest ->
                guest.connect("127.0.0.1", port, host.code, "Brave Owl")
                await(events) { it.token != null }
                val ping = marker(host.session.snapshot())
                assertFalse(host.partnerPing(ping))
                assertFalse(guest.partnerPing(ping))
            }
        }
    }

    private fun <T> await(queue: LinkedBlockingQueue<T>, match: (T) -> Boolean): T {
        val deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(8)
        while (System.nanoTime() < deadline) {
            val item = queue.poll(100, TimeUnit.MILLISECONDS)
            if (item != null && match(item)) return item
        }
        throw AssertionError("Missing expected Partner Ping event")
    }
}

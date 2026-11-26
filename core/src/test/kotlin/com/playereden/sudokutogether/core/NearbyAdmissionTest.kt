package com.playereden.sudokutogether.core

import java.io.IOException
import java.net.Socket
import java.nio.file.Files
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import org.junit.Assert.*
import org.junit.Test

class NearbyAdmissionTest {
    @Test fun aClosedHandshakeSuggestsCheckingBothAppVersions() {
        java.net.ServerSocket(0).use { server ->
            val peer = kotlin.concurrent.thread(isDaemon = true) {
                server.accept().use { socket -> Protocol.read(socket.getInputStream()) }
            }
            GuestClient().use { client ->
                val error = assertThrows(IOException::class.java) { client.connect("127.0.0.1", server.localPort, "", "Lucky Otter") }
                assertTrue(error.message.orEmpty().contains("same app version"))
            }
            peer.join(3000)
        }
    }
    @Test fun openRoomAdmitsOnePartnerWithoutAPin() {
        val game = GameSession.create(fixture(), requireCode = false)
        HostServer(game.also { it.prepareCanonicalNames("Happy Bear") }).use { host ->
            val port = host.start()
            GuestClient().use { guest ->
                guest.connect("127.0.0.1", port, "", "Lucky Otter")
                assertEquals("Lucky Otter", game.snapshot().guestName)
                GuestClient().use { extra -> assertThrows(IOException::class.java) { extra.connect("127.0.0.1", port, "", "Swift Rabbit") } }
            }
        }
    }
    @Test fun protectedRoomStillRequiresItsPin() {
        HostServer(GameSession.create(fixture(), hostName = "Happy Bear", requireCode = true)).use { host ->
            val port = host.start()
            GuestClient().use { guest ->
                assertTrue(assertThrows(IOException::class.java) { guest.connect("127.0.0.1", port, "", "Lucky Otter") }.message!!.contains("code"))
            }
            GuestClient().use { it.connect("127.0.0.1", port, host.code, "Lucky Otter") }
        }
    }
    @Test fun savedOpenPolicyKeepsTheReservedPartnersIdentity() {
        val store = AtomicSessionStore(Files.createTempDirectory("nearby-policy").resolve("host.json").toFile())
        val game = GameSession.create(fixture(), persist = store::save, requireCode = false)
        val identity = game.admitGuest("Lucky Otter")
        val saved = (store.load() as LoadResult.Loaded).data
        assertFalse(saved.requireCode)
        HostServer(GameSession(saved, store::save).also { it.prepareCanonicalNames("Happy Bear") }).use { host ->
            val port = host.start()
            GuestClient().use { stranger ->
                assertTrue(assertThrows(IOException::class.java) { stranger.connect("127.0.0.1", port, "", "Quiet Turtle") }.message!!.contains("partner"))
            }
            GuestClient().use { returning ->
                returning.connect("127.0.0.1", port, "", "Lucky Otter", identity.token)
                assertEquals(identity, host.session.saved().guest)
            }
        }
    }
    @Test fun legacySavedRoomsRemainProtected() {
        val json = Json { encodeDefaults = true }
        val old = JsonObject(json.parseToJsonElement(json.encodeToString(GameSession.create(fixture()).saved())).jsonObject.filterKeys { it != "requireCode" })
        val restored = json.decodeFromString<SavedSession>(old.toString())
        assertTrue(restored.requireCode)
        HostServer(GameSession(restored).also { it.prepareCanonicalNames("Happy Bear") }).use { host ->
            val port = host.start()
            GuestClient().use { guest -> assertThrows(IOException::class.java) { guest.connect("127.0.0.1", port, "", "Lucky Otter") } }
        }
    }
    @Test fun previousProtocolReceivesAVersionErrorBeforeAdmission() {
        val game = GameSession.create(fixture(), requireCode = false)
        HostServer(game.also { it.prepareCanonicalNames("Happy Bear") }).use { host ->
            Socket("127.0.0.1", host.start()).use { socket ->
                socket.soTimeout = 3000
                Protocol.write(socket.getOutputStream(), Message("hello", version = 7, name = "Lucky Otter"))
                val reply = Protocol.read(socket.getInputStream())
                assertEquals("error", reply.type)
                assertTrue(reply.text.contains("same app version"))
                assertNull(reply.snapshot)
                assertNull(reply.token)
                assertNull(game.saved().guest)
            }
        }
    }
    @Test fun currentProtocolSharesOceanPortraitsOnJoin() {
        val hostLook = Appearance(portrait = "ocean_narwhal")
        val guestLook = Appearance(portrait = "ocean_octopus")
        val game = GameSession.create(fixture(), hostAppearance = hostLook, requireCode = false)
        HostServer(game.also { it.prepareCanonicalNames("Happy Bear") }).use { host ->
            Socket("127.0.0.1", host.start()).use { socket ->
                socket.soTimeout = 3000
                Protocol.write(socket.getOutputStream(), Message("hello", name = "Lucky Otter", appearance = guestLook))
                val reply = Protocol.read(socket.getInputStream())
                assertEquals("welcome", reply.type)
                assertEquals(WIRE_PROTOCOL_VERSION, reply.version)
                assertEquals(hostLook, reply.snapshot?.hostAppearance)
                assertEquals(guestLook, reply.snapshot?.guestAppearance)
            }
        }
    }
}

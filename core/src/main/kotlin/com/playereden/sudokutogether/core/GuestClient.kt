package com.playereden.sudokutogether.core

import java.net.Socket
import java.net.InetSocketAddress
import java.io.IOException
import java.util.concurrent.Executors

class GuestClient(private val socketFactory: () -> Socket = { Socket() }, private val listener: (ClientEvent) -> Unit = {}) : AutoCloseable {
    private val reader = Executors.newSingleThreadExecutor(threads("sudoku-input"))
    @Volatile private var link: Link? = null
    @Volatile private var latestSnapshot: Snapshot? = null
    @Volatile private var latestStatus: RoomStatus? = null
    private fun emit(event: ClientEvent) { runCatching { listener(event) } }
    fun connect(host: String, port: Int, code: String, name: String, resumeToken: String? = null, appearance: Appearance = Appearance(), expectedSessionId: String? = null) {
        require(Cosmetics.valid(appearance))
        if (NameCollection.parseCanonical(name) == null) throw IOException("Choose an approved player name.")
        if (host.isBlank() || port !in 1..65535) throw IOException("Enter a valid host address and port.")
        check(link == null) { "Already connected" }
        val socket = socketFactory()
        try {
            socket.connect(InetSocketAddress(host, port), 5000); socket.soTimeout = 8000; socket.tcpNoDelay = true
            Protocol.write(socket.getOutputStream(), Message("hello", code = code, name = name, token = resumeToken, appearance = appearance))
            val welcome = try { Protocol.read(socket.getInputStream()) }
                catch (e: java.io.EOFException) {
                    throw IOException("Connection closed. Check that both phones use the same app version, then try again.", e)
                }
            if (welcome.type == "error") throw IOException(approvedRoomError(welcome.text))
            if (welcome.type != "welcome" || welcome.version != WIRE_PROTOCOL_VERSION || welcome.snapshot == null || welcome.status == null || welcome.token == null) throw IOException("The host is running an incompatible app version.")
            validateNetworkIdentity(welcome.snapshot)
            validateSnapshot(welcome.snapshot)
            if (expectedSessionId != null && welcome.snapshot.sessionId != expectedSessionId)
                throw IOException("This is a different puzzle room. Rejoin your original room or start a new join.")
            if (welcome.token.length !in 32..128) throw IOException("Invalid rejoin credential")
            socket.soTimeout = 10_000
            val current = Link(socket) { drop(it, "Connection paused. Reconnect to the host to continue.") }; link = current
            latestSnapshot = welcome.snapshot; latestStatus = welcome.status
            emit(ClientEvent(welcome.snapshot, welcome.status, welcome.token, history = welcome.lines))
            reader.execute {
                var latest: Snapshot = welcome.snapshot
                try {
                    while (!socket.isClosed) {
                        val message = Protocol.read(socket.getInputStream()); current.lastRead = System.nanoTime()
                        if (message.version != WIRE_PROTOCOL_VERSION) throw IOException("Install the same app version on both phones.")
                        when (message.type) {
                            "state" -> {
                                val snapshot = message.snapshot ?: throw IOException("Missing puzzle")
                                val status = message.status ?: throw IOException("Missing room state")
                                validateNetworkIdentity(snapshot)
                                message.outcome?.snapshot?.let(::validateNetworkIdentity)
                                validateSnapshot(snapshot)
                                if (snapshot.sessionId != latest.sessionId || (snapshot.epoch == latest.epoch && snapshot.revision < latest.revision)) throw IOException("Out-of-order puzzle state")
                                latest = snapshot; latestSnapshot = snapshot; latestStatus = status; emit(ClientEvent(snapshot, status, outcome = message.outcome))
                            }
                            "select" -> {
                                if (message.cell !in -1..80) throw IOException("Invalid selection")
                                emit(ClientEvent(selected = message.cell))
                            }
                            "chat" -> {
                                val line = message.line ?: throw IOException("Invalid chat message")
                                if (line.player !in listOf(HOST, GUEST) || line.text.isBlank()) throw IOException("Invalid chat sender")
                                emit(ClientEvent(chat = line))
                            }
                            "partner_ping" -> {
                                val marker = message.partnerPing ?: throw IOException("Missing partner marker")
                                if (marker.matches(latest) && latestStatus?.let { it.hostActive && it.guestActive } == true)
                                    emit(ClientEvent(partnerPing = marker))
                            }
                            "ping" -> current.send(Message("pong"))
                            "error" -> throw IOException(approvedRoomError(message.text))
                            else -> throw IOException("Unsupported game message")
                        }
                    }
                } catch (_: Exception) { drop(current, "Connection paused. Reconnect to the host to continue.") }
            }
        } catch (e: Exception) { socket.close(); throw if (e is IOException) e else IOException("Could not join this room.", e) }
    }
    private fun drop(current: Link, reason: String) {
        synchronized(this) {
            if (link !== current) return
            link = null; latestSnapshot = null; latestStatus = null; current.close(); emit(ClientEvent(disconnected = true, message = reason))
        }
    }
    fun submit(operation: Operation) {
        val current = link ?: throw IOException("Reconnect before entering a move.")
        current.send(Message("move", operation = operation))
    }
    fun select(cell: Int) { if (cell in -1..80) link?.send(Message("select", cell = cell)) }
    fun partnerPing(marker: PartnerPing): Boolean {
        val current = link ?: return false
        val snapshot = latestSnapshot ?: return false
        if (!marker.matches(snapshot) || latestStatus?.let { it.hostActive && it.guestActive } != true) return false
        current.send(Message("partner_ping", partnerPing = marker))
        return true
    }
    fun sendChat(text: String) {
        require(QuickChat.isPreset(text)) { "Choose an approved Quick Chat message." }
        (link ?: throw IOException("Reconnect to chat.")).send(Message("chat", text = text))
    }
    fun changeAppearance(appearance: Appearance) {
        require(Cosmetics.valid(appearance))
        (link ?: throw IOException("Reconnect to update your style.")).send(Message("style", appearance = appearance))
    }
    fun setForeground(active: Boolean) { link?.send(Message("presence", active = active)) }
    override fun close() {
        synchronized(this) { val current = link; link = null; latestSnapshot = null; latestStatus = null; current?.close(); reader.shutdownNow() }
    }
}

private fun validateNetworkIdentity(snapshot: Snapshot) {
    if (NameCollection.parseCanonical(snapshot.hostName) == null ||
        snapshot.guestName?.let { NameCollection.parseCanonical(it) == null } == true)
        throw IOException("Invalid player name in room state.")
}

private fun approvedRoomError(text: String): String = if (text in setOf(
    "This room has closed.", "Install the same app version on both phones.",
    "This room already has two players.", "Too many incorrect codes. Try again in a minute.",
    "That room code is incorrect.", "Choose an approved player name.",
    "This game already has a partner. Use the same phone to rejoin, or ask the host to replace the partner.",
    "That saved invitation is no longer valid. Join again with the room code."
)) text else "Could not join this room."

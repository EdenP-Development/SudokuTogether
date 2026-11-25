package com.playereden.sudokutogether

import com.playereden.sudokutogether.core.GUEST
import com.playereden.sudokutogether.core.HOST

/** Presentation only: the authoritative session still controls move eligibility. */
internal data class CoopPresence(val label: String, val attention: Boolean = false, val reconnect: Boolean = false)

internal fun coopPresence(state: GameUiState): CoopPresence {
    val snapshot = state.snapshot ?: return CoopPresence("Opening room…")
    val partner = if (state.role == HOST) snapshot.guestName else snapshot.hostName
    if (state.role == GUEST && !state.connected) return CoopPresence(
        if (snapshot.completed) "Puzzle complete. Reconnect for the next puzzle."
        else "Connection lost. Rejoin to continue.", true, true)
    if (snapshot.completed) return CoopPresence("Solved together. Nice work!")
    if (!state.status.hostActive) return CoopPresence("Paused while the host is away", true)
    if (state.status.reserved && !state.status.guestActive) return CoopPresence("Paused while your partner is away", true)
    if (partner == null) return CoopPresence("Waiting for your partner to join")
    return CoopPresence("Solving with $partner")
}

package com.playereden.sudokutogether

import com.playereden.sudokutogether.core.*

// Keep protocol/storage identifiers unchanged; translate only at the UI boundary.
internal fun historyModeLabel(mode: String): String = when (mode) {
    SOLO -> "Solo"
    DAILY -> "Daily"
    COOP -> "Co-op"
    VERSUS -> "Versus"
    else -> "Game"
}
internal fun historyResultLabel(result: String): String = when (result) {
    "SOLVED" -> "Solved"
    "WIN" -> "Won"
    "LOSS" -> "Lost"
    "DRAW" -> "Draw"
    else -> "Finished"
}

// A network peer or OS exception can supply arbitrary text. Only known
// actionable responses are permitted through to the player.
internal fun nearbyJoinFailureMessage(message: String?): String = when (message) {
    "That room code is incorrect." -> "Incorrect room code. Try again."
    "Too many incorrect codes. Try again in a minute." -> message
    "This room already has two players." -> message
    "This room has closed." -> "This game has closed. Ask your partner to start another."
    "This game already has a partner. Use the same phone to rejoin, or ask the host to replace the partner." -> message
    "That saved invitation is no longer valid. Join again with the room code." -> message
    "Install the same app version on both phones.",
    "The host is running an incompatible app version.",
    "Connection closed. Check that both phones use the same app version, then try again." -> "Check that both phones have the same app version, then try again."
    "Connect both phones to the same home Wi-Fi, then join again.",
    "Wi-Fi changed. Reconnect both phones and join again.",
    "Enter a valid host address and port." -> message
    else -> "Couldn't reach the host. Check Wi-Fi and try again."
}

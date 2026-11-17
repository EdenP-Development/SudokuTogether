package com.playereden.sudokutogether

import androidx.compose.ui.graphics.Color

/**
 * Explicit catalog-to-renderer metadata for shipped boards and ink colors.
 * Every ID in core Cosmetics.boards/inks must appear here (see unit tests).
 * Unknown network/legacy IDs still display the safe Classic/Forest defaults.
 */
internal data class BoardPalette(val fill: Color, val dark: Boolean)
internal data class InkPalette(val light: Color, val dark: Color)

internal val boardPalettes: Map<String, BoardPalette> = linkedMapOf(
    "classic" to BoardPalette(Paper, false),
    "linen" to BoardPalette(Color(0xFFFFF6DF), false),
    "midnight" to BoardPalette(Color(0xFF152A36), true),
    "circuit" to BoardPalette(Color(0xFF102A2B), true)
)

internal val inkPalettes: Map<String, InkPalette> = linkedMapOf(
    "forest" to InkPalette(Forest, Color(0xFF8DE8BB)),
    "cobalt" to InkPalette(Color(0xFF205BAC), Color(0xFF97D6FF)),
    "amber" to InkPalette(Color(0xFF986018), Color(0xFFFFD386)),
    "rose" to InkPalette(Color(0xFFB34B68), Color(0xFFFFA8B9)),
    "violet" to InkPalette(Color(0xFF7644A5), Color(0xFFD4B3FF)),
    "lagoon" to InkPalette(Color(0xFF156567), Color(0xFF8DE3DB)),
    "clay" to InkPalette(Color(0xFF9D362D), Color(0xFFFFC1AD)),
    "slate" to InkPalette(Color(0xFF4B5D76), Color(0xFFBBCDE8))
)

internal fun boardIsDark(id: String): Boolean = boardPalettes[id]?.dark ?: false

fun boardFillFor(id: String): Color = boardPalettes[id]?.fill ?: Paper

fun inkFor(id: String, darkBoard: Boolean): Color {
    val palette = inkPalettes[id] ?: inkPalettes.getValue("forest")
    return if (darkBoard) palette.dark else palette.light
}

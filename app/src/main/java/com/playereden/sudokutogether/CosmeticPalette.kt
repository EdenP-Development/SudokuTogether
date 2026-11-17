package com.playereden.sudokutogether

import androidx.compose.ui.graphics.Color

/**
 * A complete, curated board appearance shared by the playable 9x9 grid
 * and the Collection preview. These are display tokens, never game rules.
 *
 * Existing color values below are deliberately preserved for Beta 1.1.
 * Any new board must provide cell-state colors as well as its background.
 */
internal data class BoardPalette(
    val fill: Color,
    val dark: Boolean,
    val thinRule: Color,
    val thickRule: Color,
    val clueInk: Color,
    val selectedCell: Color,
    val partnerCell: Color,
    val conflictCell: Color,
    val matchingNumberCell: Color,
    val relatedCell: Color,
    val proofOutline: Color,
    val conflictInk: Color,
    val previewRule: Color
)

internal data class InkPalette(val light: Color, val dark: Color)

private fun lightBoard(fill: Color): BoardPalette = BoardPalette(
    fill = fill,
    dark = false,
    thinRule = Rule,
    thickRule = Forest,
    clueInk = Ink,
    selectedCell = AmberLight,
    partnerCell = TealLight,
    conflictCell = Color(0xFFF8E7DF),
    matchingNumberCell = Color(0xFFF5EEDB),
    relatedCell = Color(0xFFF0F3EB),
    proofOutline = Gold,
    conflictInk = Brick,
    previewRule = Rule
)

private fun darkBoard(fill: Color): BoardPalette = BoardPalette(
    fill = fill,
    dark = true,
    thinRule = Color(0xFF49616B),
    thickRule = Color(0xFFA9C8C3),
    clueInk = Color(0xFFF2F2E9),
    selectedCell = Color(0xFF555336),
    partnerCell = Color(0xFF305C55),
    conflictCell = Color(0xFF63362C),
    matchingNumberCell = Color(0xFF37473B),
    relatedCell = Color(0xFF213B3E),
    proofOutline = Color(0xFFFFD386),
    conflictInk = Color(0xFFFFB7A6),
    previewRule = Color(0xFF789690)
)

/** Explicit IDs: adding a catalog board without a palette fails unit tests. */
internal val boardPalettes: Map<String, BoardPalette> = linkedMapOf(
    "classic" to lightBoard(Paper),
    "linen" to lightBoard(Color(0xFFFFF6DF)),
    "midnight" to darkBoard(Color(0xFF152A36)),
    "circuit" to darkBoard(Color(0xFF102A2B))
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

internal fun boardPalette(id: String): BoardPalette = boardPalettes[id] ?: boardPalettes.getValue("classic")
internal fun boardIsDark(id: String): Boolean = boardPalette(id).dark

fun boardFillFor(id: String): Color = boardPalette(id).fill

fun inkFor(id: String, darkBoard: Boolean): Color {
    val palette = inkPalettes[id] ?: inkPalettes.getValue("forest")
    return if (darkBoard) palette.dark else palette.light
}

package com.playereden.sudokutogether

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontListFontFamily
import com.playereden.sudokutogether.core.Appearance
import com.playereden.sudokutogether.core.Cosmetics
import org.junit.Assert.*
import org.junit.Test

/**
 * 11 boards × 10 inks × 9 fonts = 990 real catalogue combinations.
 * Test all six possible cell backgrounds, not only an unselected paper square.
 * This is a deterministic color/ID gate; Android typography layout is tested
 * separately using the Compose renderer at normal/large system font scales.
 */
class Beta2StyleCompatibilityMatrixTest {
    private val oldIds = setOf("classic", "linen", "midnight", "circuit",
        "parchment", "ocean", "graphite")
    private val newIds = setOf("moss", "rosewood", "pebble", "tidal")

    private fun contrast(a: Color, b: Color): Float {
        val x = a.luminance()
        val y = b.luminance()
        return (maxOf(x, y) + 0.05f) / (minOf(x, y) + 0.05f)
    }

    @Test fun everyOneOf990EquippedStylesPreservesIdentityAndRenderingContracts() {
        assertEquals(11, Cosmetics.boards.size)
        assertEquals(10, Cosmetics.inks.size)
        assertEquals(9, Cosmetics.fonts.size)
        assertEquals(oldIds + newIds, Cosmetics.boards.map { it.id }.toSet())
        assertEquals(Cosmetics.boards.map { it.id }.toSet(), boardPalettes.keys)
        assertEquals(Cosmetics.inks.map { it.id }.toSet(), inkPalettes.keys)

        val families = Cosmetics.fonts.associate { it.id to fontFor(it.id) }
        assertEquals(9, families.values.toSet().size)
        assertTrue(families.values.all { it is FontListFontFamily })

        var combinations = 0
        var states = 0
        var legacyHighlightsBelowFourPointFive = 0

        for (board in Cosmetics.boards) {
            val p = boardPalette(board.id)
            val statesToInspect = linkedMapOf(
                "normal" to p.fill,
                "selected" to p.selectedCell,
                "partner" to p.partnerCell,
                "conflict" to p.conflictCell,
                "matching" to p.matchingNumberCell,
                "related" to p.relatedCell
            )
            assertEquals(board.id, p.adaptiveNumberContrast, board.id in newIds)
            for (background in statesToInspect.values) {
                assertTrue("${board.id}: clue text", contrast(p.clueInk, background) >= 4.5f)
                assertTrue("${board.id}: conflict digit",
                    contrast(conflictDigitInk(background, p), background) >= 4.5f)
                assertTrue("${board.id}: crossed-off note",
                    contrast(crossedOffNoteInk(background, p), background) >= 4.5f)
            }
            for (ink in Cosmetics.inks) {
                val color = inkFor(ink.id, p.dark)
                for (font in Cosmetics.fonts) {
                    val equipped = Appearance(board = board.id, ink = ink.id, font = font.id)
                    assertEquals(board.id, equipped.board)
                    assertEquals(ink.id, equipped.ink)
                    assertEquals(font.id, equipped.font)
                    assertEquals(fontFor(font.id), families.getValue(font.id))
                    assertEquals(equipped, equipped.withNumberDisplay(false))
                    assertEquals(equipped.copy(font = "clear", ink = "forest"),
                        equipped.withNumberDisplay(true))
                    for ((state, bg) in statesToInspect) {
                        val pencil = pencilNoteInk(color, bg, p)
                        assertTrue("${board.id}/${ink.id}/${font.id}/$state: pencil",
                            contrast(pencil, bg) >= 4.5f)
                        // A conflict overrides the usual selected/partner fill but
                        // keeps the same conflict semantics and saved ink ID.
                        assertTrue("${board.id}/${ink.id}/${font.id}/$state: conflict",
                            contrast(conflictDigitInk(bg, p), bg) >= 4.5f)
                        if (state != "conflict") {
                            val displayed = legibleBoardDigitInk(color, bg, p)
                            val measured = contrast(displayed, bg)
                            val floor = 4.49f
                            assertTrue("${board.id}/${ink.id}/${font.id}/$state: digit $measured",
                                measured >= floor)
                            if (!p.adaptiveNumberContrast && measured < 4.5f)
                                legacyHighlightsBelowFourPointFive++
                        }
                        states++
                    }
                    combinations++
                }
            }
        }
        assertEquals(990, combinations)
        assertEquals(5940, states)
        // Base-paper colors and saved ink IDs are stable. A total of 18 legacy
        // raw-ink highlighted states were initially sub-4.5; all now adapt.
        assertEquals("No rendered legacy entries may remain below 4.5", 0,
            legacyHighlightsBelowFourPointFive)
        println("BETA2_STYLE_MATRIX: 990 combos / 5940 cell states; " +
            "rendered legacy entry cases below 4.5:1: $legacyHighlightsBelowFourPointFive")
    }

    @Test fun classicAndLinenConflictHighlightBugIsCorrectedWithoutPaletteMutation() {
        for (id in listOf("classic", "linen")) {
            val palette = boardPalette(id)
            assertTrue(contrast(palette.conflictInk, palette.selectedCell) < 4.5f)
            assertTrue(contrast(palette.conflictInk, palette.partnerCell) < 4.5f)
            for (highlight in listOf(palette.selectedCell, palette.partnerCell)) {
                val fixed = conflictDigitInk(highlight, palette)
                assertNotEquals(palette.conflictInk, fixed)
                assertTrue(contrast(fixed, highlight) >= 4.5f)
            }
            assertEquals(palette.conflictInk, conflictDigitInk(palette.conflictCell, palette))
            assertEquals(Color(0xFFAB4C42), palette.conflictInk)
            assertFalse(palette.adaptiveNumberContrast)
        }
        // Existing unknown-ID and legacy appearance fallback rules remain.
        assertEquals(boardPalette("classic"), boardPalette("not-a-theme"))
        assertEquals(fontFor("clear"), fontFor("new-unknown-font"))
        assertEquals(inkFor("forest", false), inkFor("unknown-ink", false))
    }
}

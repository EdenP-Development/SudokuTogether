package com.playereden.sudokutogether

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.playereden.sudokutogether.core.Cosmetics
import org.junit.Assert.*
import org.junit.Test

/** New themes must have real state palettes, legible clues and editable ink previews. */
class Beta2FourBoardPalettesTest {
    private val additions = listOf("moss", "rosewood", "pebble", "tidal")
    private fun contrast(a: Color, b: Color): Float {
        val high = maxOf(a.luminance(), b.luminance())
        val low = minOf(a.luminance(), b.luminance())
        return (high + .05f) / (low + .05f)
    }

    @Test fun fourNewThemesAreDistinctCompleteAndSafe() {
        assertEquals(Cosmetics.boards.map { it.id }.toSet(), boardPalettes.keys)
        assertEquals(11, boardPalettes.size)
        assertEquals(11, boardPalettes.values.map { it.fill }.toSet().size)
        assertEquals(setOf("midnight", "circuit", "graphite", "rosewood", "tidal"),
            boardPalettes.filterValues { it.dark }.keys)
        for (id in additions) {
            val p = boardPalette(id)
            assertTrue(id, p.adaptiveNumberContrast)
            assertTrue("$id clue", contrast(p.clueInk, p.fill) >= 7f)
            assertTrue("$id conflict", contrast(p.conflictInk, p.conflictCell) >= 4.5f)
            assertNotEquals(p.fill, p.selectedCell)
            assertNotEquals(p.selectedCell, p.partnerCell)
        }
    }

    @Test fun allTenNumberInksHaveAccessibleRenderedDigitAndPencilContrast() {
        for (id in additions) {
            val p = boardPalette(id)
            val surfaces = listOf(p.fill, p.selectedCell, p.partnerCell,
                p.matchingNumberCell, p.relatedCell)
            for (ink in Cosmetics.inks) {
                val authored = inkFor(ink.id, p.dark)
                for (bg in surfaces) {
                    val digit = legibleBoardDigitInk(authored, bg, p)
                    val note = pencilNoteInk(authored, bg, p)
                    assertTrue("$id / ${ink.id} digit", contrast(digit, bg) >= 4.49f)
                    assertTrue("$id / ${ink.id} note", contrast(note, bg) >= 4.49f)
                }
            }
        }
    }

    @Test fun legacyBoardPaperAndInkColorsRemainWhileFaintHighlightsGetContrastLift() {
        for (id in listOf("classic", "linen", "midnight", "circuit",
            "parchment", "ocean", "graphite")) {
            val p = boardPalette(id)
            assertFalse("$id legacy palette must remain unchanged", p.adaptiveNumberContrast)
            val original = inkFor("rose", p.dark)
            // Existing paper colors and original ink IDs are unchanged.
            assertEquals(original, legibleBoardDigitInk(original, p.fill, p))
            val highlighted = legibleBoardDigitInk(original, p.selectedCell, p)
            if (contrast(original, p.selectedCell) < 4.5f) {
                assertNotEquals(original, highlighted)
                assertTrue(contrast(highlighted, p.selectedCell) >= 4.49f)
            } else {
                assertEquals(original, highlighted)
            }
        }
        assertEquals(boardFillFor("classic"), boardFillFor("unknown"))
    }
}

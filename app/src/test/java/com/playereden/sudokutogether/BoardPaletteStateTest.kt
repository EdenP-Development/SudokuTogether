package com.playereden.sudokutogether

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.playereden.sudokutogether.core.Cosmetics
import org.junit.Assert.*
import org.junit.Test

/** Guard the exact pre-expansion board states while new themes are designed. */
class BoardPaletteStateTest {
    @Test fun allRegisteredBoardsHaveCompleteStatePalettesAndSafeUnknownFallback() {
        assertEquals(Cosmetics.boards.map { it.id }.toSet(), boardPalettes.keys)
        assertEquals(boardPalette("classic"), boardPalette("not-a-theme"))
        val light = boardPalette("classic")
        val dark = boardPalette("midnight")
        assertEquals(Color(0xFFD8DFD2), light.thinRule)
        assertEquals(Color(0xFF214F3F), light.thickRule)
        assertEquals(Color(0xFF263D36), light.clueInk)
        assertEquals(Color(0xFFF3E5BF), light.selectedCell)
        assertEquals(Color(0xFFDCECE4), light.partnerCell)
        assertEquals(Color(0xFFF8E7DF), light.conflictCell)
        assertEquals(Color(0xFFF5EEDB), light.matchingNumberCell)
        assertEquals(Color(0xFFF0F3EB), light.relatedCell)
        assertEquals(Color(0xFF8A6729), light.proofOutline)
        assertEquals(Color(0xFFAB4C42), light.conflictInk)
        assertEquals(Color(0xFFD8DFD2), light.previewRule)
        assertEquals(Color(0xFF49616B), dark.thinRule)
        assertEquals(Color(0xFFA9C8C3), dark.thickRule)
        assertEquals(Color(0xFFF2F2E9), dark.clueInk)
        assertEquals(Color(0xFF555336), dark.selectedCell)
        assertEquals(Color(0xFF305C55), dark.partnerCell)
        assertEquals(Color(0xFF63362C), dark.conflictCell)
        assertEquals(Color(0xFF37473B), dark.matchingNumberCell)
        assertEquals(Color(0xFF213B3E), dark.relatedCell)
        assertEquals(Color(0xFFFFD386), dark.proofOutline)
        assertEquals(Color(0xFFFFB7A6), dark.conflictInk)
        assertEquals(Color(0xFF789690), dark.previewRule)
        assertEquals(light.copy(fill = Color(0xFFFFF6DF)), boardPalette("linen"))
        assertEquals(dark.copy(fill = Color(0xFF102A2B)), boardPalette("circuit"))
    }

    @Test fun currentEntryInksStayReadableAcrossExistingBoardCellStates() {
        // The old palette's observed floor is ~4.06:1 on some highlights.
        // This is a *regression guard*, not a claim of WCAG 4.5 compliance.
        // New theme pilots should target >= 4.5:1 before shipping.
        for ((boardId, board) in boardPalettes) {
            val backgrounds = listOf(
                board.fill, board.selectedCell, board.partnerCell,
                board.matchingNumberCell, board.relatedCell
            )
            for (ink in Cosmetics.inks) {
                val authoredInk = inkFor(ink.id, board.dark)
                for (background in backgrounds) {
                    // New themes use the exact same rendered-digit contrast helper
                    // as PlayScreen. Keep the historical raw-ink floor on legacy boards.
                    val foreground = legibleBoardDigitInk(authoredInk, background, board)
                    assertTrue("$boardId / ${ink.id}: contrast too low for $background",
                        contrast(foreground, background) >= if (board.adaptiveNumberContrast) 4.49f else 4.0f)
                }
            }
            assertTrue("$boardId clues", contrast(board.clueInk, board.fill) >= 4.5f)
            assertTrue("$boardId conflicts", contrast(board.conflictInk, board.conflictCell) >= 4.5f)
        }
    }

    private fun contrast(a: Color, b: Color): Float {
        val l1 = a.luminance()
        val l2 = b.luminance()
        return (maxOf(l1, l2) + .05f) / (minOf(l1, l2) + .05f)
    }
}

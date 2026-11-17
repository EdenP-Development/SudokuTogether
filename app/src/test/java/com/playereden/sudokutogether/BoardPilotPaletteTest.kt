package com.playereden.sudokutogether

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.playereden.sudokutogether.core.Cosmetics
import org.junit.Assert.*
import org.junit.Test

/** All three pilot skins must remain distinct and legible with every bundled ink. */
class BoardPilotPaletteTest {
    @Test fun parchmentAndGraphiteUseTheDocumentedPalettesAndOldThemesRemain() {
        val parchment = boardPalette("parchment")
        val graphite = boardPalette("graphite")
        val ocean = boardPalette("ocean")
        assertFalse(parchment.dark)
        assertTrue(graphite.dark)
        assertFalse(ocean.dark)
        assertEquals(Color(0xFFFCF4E4), parchment.fill)
        assertEquals(Color(0xFF293137), graphite.fill)
        assertEquals(Color(0xFFF1F8FA), ocean.fill)
        assertEquals(Color(0xFF346F7D), ocean.thickRule)
        assertEquals(Color(0xFFF9FCFF), ocean.selectedCell)
        assertEquals(Color(0xFFF6FCFA), ocean.partnerCell)
        assertEquals(Color(0xFF963B34), ocean.conflictInk)
        assertEquals(Color(0xFF6A5648), parchment.thickRule)
        assertEquals(Color(0xFFA9B7BC), graphite.thickRule)
        assertEquals(Color(0xFFFFF9EF), parchment.selectedCell)
        assertEquals(Color(0xFF3F4748), graphite.selectedCell)
        assertEquals(Color(0xFFF5FAF5), parchment.partnerCell)
        assertEquals(Color(0xFF34454B), graphite.partnerCell)
        assertEquals(Color(0xFF8D362B), parchment.conflictInk)
        assertEquals(Color(0xFFFFC0BA), graphite.conflictInk)
        assertEquals("No missing or extra palettes", Cosmetics.boards.map { it.id }.toSet(), boardPalettes.keys)
        assertEquals(7, Cosmetics.boards.size)
        assertEquals(7, Cosmetics.boards.map { boardFillFor(it.id) }.toSet().size)

        assertEquals(Color(0xFF152A36), boardFillFor("midnight"))
        assertEquals(Color(0xFF102A2B), boardFillFor("circuit"))
        assertEquals(Paper, boardFillFor("classic"))
        assertEquals(Color(0xFFFFF6DF), boardFillFor("linen"))
    }

    @Test fun everyExistingInkRemainsAtLeastFourPointFiveOnNewBoardStates() {
        val pilotIds = listOf("parchment", "ocean", "graphite")
        for (boardId in pilotIds) {
            val board = boardPalette(boardId)
            val normalAndHighlights = listOf(
                board.fill, board.selectedCell, board.partnerCell,
                board.matchingNumberCell, board.relatedCell
            )
            for (ink in Cosmetics.inks) {
                val color = inkFor(ink.id, board.dark)
                for (background in normalAndHighlights) {
                    assertTrue(
                        "$boardId with ${ink.id} fails 4.5:1 on $background",
                        contrast(color, background) >= 4.5f
                    )
                }
            }
            assertTrue("$boardId clues", contrast(board.clueInk, board.fill) >= 4.5f)
            assertTrue("$boardId conflict text", contrast(board.conflictInk, board.conflictCell) >= 4.5f)
        }
    }

    @Test fun newInksStayAtLeastFourPointFiveOnAllRegisteredBoardStates() {
        for (inkId in listOf("sepia", "indigo")) {
            for ((boardId, board) in boardPalettes) {
                val ink = inkFor(inkId, board.dark)
                for (background in listOf(
                    board.fill, board.selectedCell, board.partnerCell,
                    board.matchingNumberCell, board.relatedCell
                )) {
                    assertTrue(
                        "$inkId / $boardId insufficient contrast on $background",
                        contrast(ink, background) >= 4.5f
                    )
                }
            }
        }
        assertEquals(Color(0xFF76513D), inkFor("sepia", false))
        assertEquals(Color(0xFFE9C29D), inkFor("sepia", true))
        assertEquals(Color(0xFF3D4FA0), inkFor("indigo", false))
        assertEquals(Color(0xFFC1CEFF), inkFor("indigo", true))
    }

    private fun contrast(a: Color, b: Color): Float {
        val l1 = a.luminance()
        val l2 = b.luminance()
        return (maxOf(l1, l2) + .05f) / (minOf(l1, l2) + .05f)
    }
}

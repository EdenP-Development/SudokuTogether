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
        // The seven existing color recipes are still pinned above; four more
        // complete palette records are now registered for isolated Beta 2 QA.
        assertEquals(11, Cosmetics.boards.size)
        assertEquals(11, Cosmetics.boards.map { boardFillFor(it.id) }.toSet().size)

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
                val authored = inkFor(inkId, board.dark)
                for (background in listOf(
                    board.fill, board.selectedCell, board.partnerCell,
                    board.matchingNumberCell, board.relatedCell
                )) {
                    // Actual displayed digits on new materials adapt locally;
                    // original seven boards still retain their original inks.
                    val rendered = legibleBoardDigitInk(authored, background, board)
                    assertTrue(
                        "$inkId / $boardId insufficient contrast on $background",
                        contrast(rendered, background) >= 4.5f
                    )
                }
            }
        }
        assertEquals(Color(0xFF76513D), inkFor("sepia", false))
        assertEquals(Color(0xFFE9C29D), inkFor("sepia", true))
        assertEquals(Color(0xFF3D4FA0), inkFor("indigo", false))
        assertEquals(Color(0xFFC1CEFF), inkFor("indigo", true))
    }

    @Test fun pencilMarksMeetFourPointFiveToOneOnAllRegisteredBoardHighlights() {
        for ((boardId, board) in boardPalettes) {
            for (background in listOf(
                board.fill, board.selectedCell, board.partnerCell,
                board.matchingNumberCell, board.relatedCell, board.conflictCell
            )) {
                // A small pencil mark is often harder to read than a full-size
                // digit. Check its *rendered* ink, not the unchanged saved ink.
                for (ink in Cosmetics.inks) {
                    val original = inkFor(ink.id, board.dark)
                    val rendered = pencilNoteInk(original, background, board)
                    assertTrue(
                        "$boardId / ${ink.id} pencil mark on $background is too faint: " +
                            contrast(rendered, background),
                        contrast(rendered, background) >= 4.5f
                    )
                    if (!board.dark) {
                        // New light materials may need a small contrast lift on
                        // tinted highlights. Legacy light-board notes never change.
                        if (!board.adaptiveNumberContrast || contrast(Muted, background) >= 4.52f)
                            assertEquals("Legible light-board note colors stay familiar", Muted, rendered)
                    } else if (contrast(original, background) >= 4.52f) {
                        assertEquals("Legible dark-board ink must not be altered", original, rendered)
                    }
                }
            }
        }
    }

    @Test fun onlyFaintHighlightedPencilMarksReceiveAContrastLift() {
        val midnight = boardPalette("midnight")
        val rose = inkFor("rose", true)
        val violet = inkFor("violet", true)
        assertTrue(contrast(rose, midnight.partnerCell) < 4.5f)
        assertTrue(contrast(violet, midnight.partnerCell) < 4.5f)
        val adjustedRose = pencilNoteInk(rose, midnight.partnerCell, midnight)
        val adjustedViolet = pencilNoteInk(violet, midnight.partnerCell, midnight)
        assertNotEquals(rose, adjustedRose)
        assertNotEquals(violet, adjustedViolet)
        assertTrue(contrast(adjustedRose, midnight.partnerCell) >= 4.5f)
        assertTrue(contrast(adjustedViolet, midnight.partnerCell) >= 4.5f)
        assertEquals(rose, pencilNoteInk(rose, midnight.fill, midnight))
        assertEquals(Color(0xFFFFA8B9), inkFor("rose", true))
        assertEquals(Color(0xFFD4B3FF), inkFor("violet", true))
    }

    @Test fun coachCrossedOffCandidatesStayReadableOnEveryBoardHighlight() {
        for ((boardId, board) in boardPalettes) {
            for (background in listOf(
                board.fill, board.selectedCell, board.partnerCell,
                board.matchingNumberCell, board.relatedCell, board.conflictCell
            )) {
                val color = crossedOffNoteInk(background, board)
                assertTrue(
                    "$boardId crossed-off note faint on $background: " + contrast(color, background),
                    contrast(color, background) >= 4.5f
                )
                if (contrast(board.conflictInk, background) >= 4.52f) {
                    assertEquals("Keep legible Coach red unchanged", board.conflictInk, color)
                }
            }
        }
        val classic = boardPalette("classic")
        assertTrue(contrast(classic.conflictInk, classic.selectedCell) < 4.5f)
        assertNotEquals(classic.conflictInk, crossedOffNoteInk(classic.selectedCell, classic))
        assertEquals(Brick, classic.conflictInk) // Original board palette stays unchanged.
    }

    private fun contrast(a: Color, b: Color): Float {
        val l1 = a.luminance()
        val l2 = b.luminance()
        return (maxOf(l1, l2) + .05f) / (minOf(l1, l2) + .05f)
    }
}

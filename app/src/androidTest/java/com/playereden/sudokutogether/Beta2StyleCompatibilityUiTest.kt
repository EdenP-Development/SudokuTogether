package com.playereden.sudokutogether

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.playereden.sudokutogether.core.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

/**
 * Actual Compose renderer checks, complementary to the 990-color JVM matrix.
 * No saved profiles, LAN devices or production signing are involved.
 */
class Beta2StyleCompatibilityUiTest {
    @get:Rule val compose = createComposeRule()

    private fun layoutForText(digit: Int): TextLayoutResult {
        val results = mutableListOf<TextLayoutResult>()
        compose.onNodeWithText(digit.toString(), useUnmergedTree = true)
            .performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(results) }
        assertEquals("Digit $digit returned one layout", 1, results.size)
        return results.single()
    }

    private fun layoutForPencil(index: Int, digit: Int): TextLayoutResult {
        val results = mutableListOf<TextLayoutResult>()
        compose.onNodeWithTag("pencil_cell_${index}_${digit}", useUnmergedTree = true)
            .performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(results) }
        assertEquals("Pencil $digit returned one layout", 1, results.size)
        return results.single()
    }

    private fun assertFits(result: TextLayoutResult, label: String) {
        assertEquals("$label: one line", 1, result.lineCount)
        assertFalse("$label overflow: ${result.size} / ${result.layoutInput.constraints}",
            result.hasVisualOverflow)
        assertFalse("$label width overflow", result.didOverflowWidth)
        assertFalse("$label height overflow", result.didOverflowHeight)
    }

    private fun previewMatrix(fontScale: Float) {
        val look = mutableStateOf(Appearance())
        compose.setContent {
            CompositionLocalProvider(LocalDensity provides Density(3f, fontScale)) {
                SudokuTogetherTheme {
                    Box(Modifier.width(320.dp)) {
                        StylePreview(look.value, uniformNumbers = false, allEntries = true)
                    }
                }
            }
        }
        var samples = 0
        for (board in Cosmetics.boards) for (font in Cosmetics.fonts) {
            compose.runOnIdle {
                look.value = Appearance(board = board.id, ink = "indigo", font = font.id)
            }
            compose.waitForIdle()
            for (digit in 1..9) {
                val layout = layoutForText(digit)
                assertEquals("$board / ${font.id} / $digit @ $fontScale",
                    fontFor(font.id), layout.layoutInput.style.fontFamily)
                assertFits(layout, "$board / ${font.id} / $digit @ $fontScale")
                samples++
            }
        }
        assertEquals("All 11 x 9 x 9 preview glyphs", 891, samples)
    }

    @Test fun allElevenBoardsAndNineFontsHaveUnclipped1to9PreviewsAtNormalTextSize() =
        previewMatrix(1f)

    @Test fun allElevenBoardsAndNineFontsHaveUnclipped1to9PreviewsAt200PercentTextSize() =
        previewMatrix(2f)

    @Test fun realNineByNinePencilGridKeepsAllNineFontsInsideCompactAndLargeSquares() {
        val clues = "530070000600195000098000060800060003400803001700020006060000280000419005000080079"
            .map(Char::digitToInt)
        val puzzle = Puzzle("matrix-notes", clues, "EASY")
        val cells = clues.mapIndexed { index, value ->
            Cell(value, if (index == 2) 0b111111111 else 0)
        }
        val snapshot = Snapshot("matrix-notes", "matrix-epoch", puzzle, cells, mode = SOLO)
        val look = mutableStateOf(Appearance())
        val size = mutableStateOf<Dp>(264.dp)
        compose.setContent {
            CompositionLocalProvider(LocalDensity provides Density(3f, 2f)) {
                SudokuTogetherTheme {
                    SudokuBoard(snapshot, selected = 2, partner = 3,
                        size = size.value, select = {}, boardTheme = look.value.board,
                        localStyle = look.value, solo = true, uniformNumbers = false)
                }
            }
        }
        var samples = 0
        for (boardId in listOf("classic", "linen", "moss", "rosewood", "pebble", "tidal")) {
            for (font in Cosmetics.fonts) for (dp in listOf(264.dp, 340.dp)) {
                compose.runOnIdle {
                    size.value = dp
                    look.value = Appearance(board = boardId, ink = "rose", font = font.id)
                }
                compose.waitForIdle()
                for (digit in 1..9) {
                    val layout = layoutForPencil(2, digit)
                    assertEquals("$boardId / ${font.id} pencil $digit",
                        fontFor(font.id), layout.layoutInput.style.fontFamily)
                    assertFits(layout, "$boardId / ${font.id} / $dp pencil $digit")
                    samples++
                }
            }
        }
        assertEquals("6 boards x 9 fonts x 2 sizes x 9 pencil glyphs", 972, samples)
    }
}

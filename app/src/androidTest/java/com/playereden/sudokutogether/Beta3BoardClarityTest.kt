package com.playereden.sudokutogether

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.playereden.sudokutogether.core.*
import org.junit.Rule
import org.junit.Test

class Beta3BoardClarityTest {
    @get:Rule val compose = createComposeRule()

    @Test fun selectedConflictsAndRelatedCellsHaveDistinctAccessibleStates() {
        val digits = "123456789456789123789123456234567891567891234891234567345678912678912345912345678".map { it.digitToInt() }
        val puzzle = Puzzle("board-clarity", digits.mapIndexed { i, n -> if (i == 0) 0 else n }, "EASY")
        val cells = puzzle.clues.mapIndexed { i, n -> Cell(if (i == 0) 2 else n) }
        val shown = mutableStateOf(Snapshot("clarity", "clarity-epoch", puzzle, cells, mode = SOLO))
        compose.setContent {
            SudokuTogetherTheme {
                SudokuBoard(shown.value, 0, -1, 270.dp, {}, "classic", Appearance(), true, false)
            }
        }
        fun expect(index: Int, state: String) {
            compose.onNodeWithTag("cell_$index").assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, state))
        }
        expect(0, "Selected square, conflicting entered value")
        compose.onNodeWithTag("cell_0").assertContentDescriptionEquals("Row 1 column 1, value 2")
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.Text))
            .assertHasClickAction()
        compose.onNodeWithTag("cell_1").assertContentDescriptionEquals("Row 1 column 2, clue 2")
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.Text))
        expect(1, "Fixed clue duplicated by an entry; check placed values")
        expect(2, "Related square")
        // A legal matching digit outside the selected row, column and box.
        val matching = digits.indices.first { i -> digits[i] == 2 && i / 9 >= 3 && i % 9 != 0 }
        expect(matching, "Matching number")
        compose.runOnIdle { shown.value = shown.value.copy(cells = cells.mapIndexed { i, cell -> if (i == 0) cell.copy(value = 1) else cell }) }
        expect(0, "Selected square")
        expect(1, "Related square")
        compose.runOnIdle {
            shown.value = shown.value.copy(cells = cells.mapIndexed { i, cell ->
                if (i == 0) cell.copy(value = 0, notes = 5) else cell
            })
        }
        compose.onNodeWithTag("cell_0").assertContentDescriptionEquals("Row 1 column 1, empty, notes 1 3")
            .assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.Text))
            .assertIsSelected().assertHasClickAction()
        compose.onNodeWithTag("pencil_cell_0_1", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("pencil_cell_0_3", useUnmergedTree = true).assertExists()
    }
}

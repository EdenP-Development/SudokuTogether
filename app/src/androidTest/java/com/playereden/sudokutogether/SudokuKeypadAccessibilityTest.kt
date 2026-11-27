package com.playereden.sudokutogether

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.playereden.sudokutogether.core.Cell
import com.playereden.sudokutogether.core.Puzzle
import com.playereden.sudokutogether.core.SOLO
import com.playereden.sudokutogether.core.Snapshot
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/** Numbers and pencil marks share a keypad but must not sound like the same action. */
class SudokuKeypadAccessibilityTest {
    @get:Rule val compose = createComposeRule()

    @Test fun screenReaderAnnouncesTheCorrectActionAsNoteModeChanges() {
        val solution = "123456789456789123789123456234567891567891234891234567345678912678912345912345678"
            .map(Char::digitToInt)
        val clues = solution.mapIndexed { index, digit -> if (index == 0) 0 else digit }
        val puzzle = Puzzle("keypad-accessibility", clues, "EASY")
        val snapshot = Snapshot("keypad", "keypad-epoch", puzzle, clues.map { Cell(it) }, mode = SOLO)
        val state = mutableStateOf(GameUiState(page = Page.PLAY, role = SOLO, snapshot = snapshot, selected = 0))
        var lastPressed = -1
        val actions = GameActions(
            name = {}, host = {}, showJoin = {}, home = {}, join = { _, _ -> }, select = {},
            number = { lastPressed = it }, toggleNotes = {}, erase = {}, undo = {},
            replacePartner = {}, newPuzzle = {}, solo = {}, profile = {}, hostRace = {},
            sendChat = {}, chatVisible = {}, cosmetic = { _, _ -> }, uniformNumbers = {},
            touchFeedback = {}, difficulty = {}, hint = {}, dismissHint = {},
            autoCleanNotes = {}, daily = {}, openDaily = {}
        )

        compose.setContent { SudokuTogetherTheme { SudokuApp(state.value, actions, null) } }

        val one = compose.onNodeWithTag("number_1")
        one.assertIsEnabled().assertContentDescriptionEquals("Number 1, 1 remaining")
        one.assert(SemanticsMatcher.keyNotDefined(androidx.compose.ui.semantics.SemanticsProperties.Text))
        compose.onNodeWithText("Notes").assertIsNotSelected()
        for (tool in listOf("Erase", "Undo")) {
            compose.onNodeWithText(tool).assert(
                SemanticsMatcher.keyNotDefined(androidx.compose.ui.semantics.SemanticsProperties.Selected))
        }
        compose.onNodeWithContentDescription("Request a solo Sudoku hint").assert(
            SemanticsMatcher.keyNotDefined(androidx.compose.ui.semantics.SemanticsProperties.Selected))
        compose.onNodeWithTag("number_remaining_1", useUnmergedTree = true).assertExists()
        one.performClick()
        compose.runOnIdle { assertEquals(1, lastPressed) }

        val boardBefore = compose.onNodeWithTag("board_frame").getUnclippedBoundsInRoot()
        val dockBefore = compose.onNodeWithTag("input_dock").getUnclippedBoundsInRoot()
        compose.runOnIdle { state.value = state.value.copy(notes = true) }
        compose.onNodeWithTag("keypad_mode_guidance").assertExists()
        val boardAfter = compose.onNodeWithTag("board_frame").getUnclippedBoundsInRoot()
        val dockAfter = compose.onNodeWithTag("input_dock").getUnclippedBoundsInRoot()
        assertEquals(boardBefore.right - boardBefore.left, boardAfter.right - boardAfter.left)
        assertEquals(dockBefore.bottom - dockBefore.top, dockAfter.bottom - dockAfter.top)
        one.assertIsEnabled().assertContentDescriptionEquals("Add pencil mark 1").assertIsNotSelected()
        compose.onNodeWithText("Erase").assertIsNotEnabled()
        compose.onNodeWithText("Notes on").assertIsSelected()
        compose.onNodeWithTag("number_remaining_1", useUnmergedTree = true).assertDoesNotExist()
        one.performClick()
        compose.runOnIdle { assertEquals(1, lastPressed) }

        // Simulate a saved pencil mark in the selected square, then check
        // that TalkBack announces the inverse action before it is tapped.
        compose.runOnIdle {
            val old = requireNotNull(state.value.snapshot)
            state.value = state.value.copy(snapshot = old.copy(cells = old.cells.mapIndexed { index, cell ->
                if (index == 0) cell.copy(notes = 1) else cell
            }))
        }
        one.assertContentDescriptionEquals("Remove pencil mark 1").assertIsSelected()
        compose.onNodeWithTag("number_2").assertIsNotSelected()
        compose.onNodeWithText("Erase").assertIsEnabled()
        one.performClick()
        compose.runOnIdle { assertEquals(1, lastPressed) }

        compose.runOnIdle { state.value = state.value.copy(notes = false) }
        one.assertContentDescriptionEquals("Number 1, 1 remaining")
        compose.onNodeWithTag("number_remaining_1", useUnmergedTree = true).assertExists()

        // Filling a non-clue square makes note edits invalid in both Solo
        // and multiplayer rules, while number replacement stays available.
        compose.runOnIdle {
            val old = requireNotNull(state.value.snapshot)
            state.value = state.value.copy(notes = true, snapshot = old.copy(cells = old.cells.mapIndexed { index, cell ->
                if (index == 0) cell.copy(value = 1, notes = 0) else cell
            }))
        }
        one.assertIsNotEnabled().assertIsNotSelected()
        compose.onNodeWithText("Erase the entry to add pencil marks").assertExists()
        compose.onNodeWithText("Erase").assertIsEnabled()
        compose.onNodeWithText("R1 · C1 · Erase entry before adding pencil marks").assertIsDisplayed()
        compose.runOnIdle { state.value = state.value.copy(notes = false) }
        one.assertIsEnabled()
    }
}

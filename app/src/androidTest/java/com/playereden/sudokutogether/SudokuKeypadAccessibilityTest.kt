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
        compose.onNodeWithTag("number_remaining_1").assertExists()
        one.performClick()
        compose.runOnIdle { assertEquals(1, lastPressed) }

        compose.runOnIdle { state.value = state.value.copy(notes = true) }
        one.assertIsEnabled().assertContentDescriptionEquals("Add pencil mark 1")
        compose.onNodeWithText("Notes on").assertExists()
        compose.onNodeWithTag("number_remaining_1").assertDoesNotExist()
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
        one.assertContentDescriptionEquals("Remove pencil mark 1")
        one.performClick()
        compose.runOnIdle { assertEquals(1, lastPressed) }

        compose.runOnIdle { state.value = state.value.copy(notes = false) }
        one.assertContentDescriptionEquals("Number 1, 1 remaining")
        compose.onNodeWithTag("number_remaining_1").assertExists()

        // Filling a non-clue square makes note edits invalid in both Solo
        // and multiplayer rules, while number replacement stays available.
        compose.runOnIdle {
            val old = requireNotNull(state.value.snapshot)
            state.value = state.value.copy(notes = true, snapshot = old.copy(cells = old.cells.mapIndexed { index, cell ->
                if (index == 0) cell.copy(value = 1, notes = 0) else cell
            }))
        }
        one.assertIsNotEnabled()
        compose.onNodeWithText("R1 · C1 · Erase entry before adding pencil marks").assertIsDisplayed()
        compose.runOnIdle { state.value = state.value.copy(notes = false) }
        one.assertIsEnabled()
    }
}

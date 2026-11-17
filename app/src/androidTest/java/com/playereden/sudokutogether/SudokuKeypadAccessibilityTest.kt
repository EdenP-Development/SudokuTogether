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
        val snapshot = Snapshot("keypad", "keypad-epoch", puzzle, clues.map(::Cell))
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
        one.performClick()
        compose.runOnIdle { assertEquals(1, lastPressed) }

        compose.runOnIdle { state.value = state.value.copy(notes = true) }
        one.assertIsEnabled().assertContentDescriptionEquals("Toggle pencil mark 1")
        compose.onNodeWithText("Notes on").assertExists()
        one.performClick()
        compose.runOnIdle { assertEquals(1, lastPressed) }

        compose.runOnIdle { state.value = state.value.copy(notes = false) }
        one.assertContentDescriptionEquals("Number 1, 1 remaining")
    }
}

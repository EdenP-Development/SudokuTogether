package com.playereden.sudokutogether

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.playereden.sudokutogether.core.*
import org.junit.Rule
import org.junit.Test

/** Regression cases prompted by actual Beta 1.2 screenshots on an owner Android phone. */
class PhoneReviewPolishUiTest {
    @get:Rule val compose = createComposeRule()

    private fun duplicateSixFixture(): GameUiState {
        // A fixed 6 in R2C6, and a user-entered duplicate in R2C7.
        val clues = MutableList(81) { 0 }.also { it[14] = 6 }
        val cells = clues.map { Cell(it) }.toMutableList().also { it[15] = Cell(6) }
        val puzzle = Puzzle("owner-review-duplicate", clues, "EASY")
        return GameUiState(page = Page.PLAY, role = SOLO, selected = 15,
            snapshot = Snapshot("owner-review", "round-1", puzzle, cells, mode = SOLO))
    }

    @Test fun selectedDuplicateExplainsMistakeWithoutClaimingStartingClueIsWrong() {
        val state = mutableStateOf(duplicateSixFixture())
        compose.setContent { SudokuTogetherTheme { SudokuApp(state.value, polishActions(), {}) } }

        compose.onNodeWithTag("board_context").assertTextContains("Repeated 6")
        compose.onNodeWithTag("cell_15").assert(SemanticsMatcher.expectValue(
            SemanticsProperties.StateDescription, "Selected square, conflicting entered value"))
        compose.onNodeWithTag("cell_14").assert(SemanticsMatcher.expectValue(
            SemanticsProperties.StateDescription, "Fixed clue duplicated by an entry; check placed values"))
        compose.runOnIdle { state.value = state.value.copy(selected = 14) }
        compose.onNodeWithTag("board_context").assertTextContains("Starting clue")
        compose.onNodeWithTag("cell_14").assert(SemanticsMatcher.expectValue(
            SemanticsProperties.StateDescription, "Selected fixed clue duplicated by an entry"))
    }

    @Test fun remainingCountExplanationOnlyAppearsWhenEnteringNumbers() {
        val state = mutableStateOf(duplicateSixFixture().copy(selected = 16))
        compose.setContent { SudokuTogetherTheme { SudokuApp(state.value, polishActions(), {}) } }
        compose.onNodeWithTag("keypad_remaining_explanation").assertIsDisplayed()
        compose.onNodeWithTag("number_remaining_6").assertExists()

        compose.runOnIdle { state.value = state.value.copy(notes = true) }
        compose.onNodeWithTag("keypad_remaining_explanation").assertDoesNotExist()
        compose.onNodeWithTag("number_remaining_6").assertDoesNotExist()
        compose.onNodeWithText("Notes on").assertIsDisplayed()
    }

    @Test fun firstTimeProfileInvitesStarterAnimalChoiceWithoutChangingTheDefault() {
        val state = mutableStateOf(GameUiState(page = Page.PROFILE))
        compose.setContent { SudokuTogetherTheme { SudokuApp(state.value, polishActions(), {}) } }
        compose.onNodeWithText("Try Owl, Fox, Octopus or Honeybee", substring = true).assertIsDisplayed()
        compose.onNodeWithTag("profile_icon_quick_change").assertIsDisplayed()

        compose.runOnIdle {
            state.value = state.value.copy(appearance = state.value.appearance.copy(portrait = "woodland_owl"))
        }
        compose.onNodeWithText("Explore portraits, boards & numbers").assertIsDisplayed()
        compose.onNodeWithText("Try Owl, Fox, Octopus or Honeybee", substring = true).assertDoesNotExist()
    }
}

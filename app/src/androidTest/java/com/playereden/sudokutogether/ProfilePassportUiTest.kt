package com.playereden.sudokutogether

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

/** The new player journal is an actionable destination even before the first win. */
class ProfilePassportUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun firstVisitPassportAndJournalLeadToPlay() {
        val state = mutableStateOf(GameUiState(page = Page.PROFILE))
        val actions = polishActions().copy(home = {
            state.value = state.value.copy(page = Page.HOME)
        })
        compose.setContent { SudokuTogetherTheme { SudokuApp(state.value, actions, {}) } }

        compose.onNodeWithTag("profile_player_card").assertIsDisplayed()
        compose.onNodeWithText("YOUR PLAYER JOURNAL").assertIsDisplayed()
        compose.onNodeWithText("Your first chapter starts here").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Go to puzzles").performScrollTo().performClick()
        compose.onNodeWithTag("home_book_stage").assertIsDisplayed()
    }

    @Test fun passportKeepsTheRealAppearancePicker() {
        compose.setContent {
            SudokuTogetherTheme {
                SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {})
            }
        }
        compose.onNodeWithTag("profile_icon_quick_change").assertIsDisplayed().performClick()
        compose.onNodeWithTag("nav_collection").assertIsSelected()
        compose.onNodeWithTag("collection_current_style_preview").assertIsDisplayed()
    }
}

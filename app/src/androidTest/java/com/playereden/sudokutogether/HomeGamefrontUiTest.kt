package com.playereden.sudokutogether

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

class HomeGamefrontUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun homeMenuKeepsSoloCoopVersusAndDailyActionsDiscoverable() {
        compose.setContent {
            SudokuTogetherTheme { SudokuApp(GameUiState(), polishActions(), {}) }
        }
        compose.onNodeWithTag("home_game_banner").assertExists()
        compose.onNodeWithText("Good puzzles.\nGood company.").assertExists()
        compose.onNodeWithTag("home_mode_solo").assertIsSelected()
        compose.onNodeWithText("Classic 9×9").assertIsDisplayed()
        compose.onNodeWithText("New puzzle").assertIsDisplayed()

        compose.onNodeWithTag("home_mode_together").performClick().assertIsSelected()
        compose.onNodeWithText("Shared board").assertIsDisplayed()
        compose.onNodeWithText("Start a game").assertIsDisplayed()
        compose.onNodeWithText("Join a game").assertIsDisplayed()
        compose.onNodeWithTag("require_code").assertExists()

        compose.onNodeWithTag("home_mode_versus").performClick().assertIsSelected()
        compose.onNodeWithText("Race to finish").assertIsDisplayed()
        compose.onNodeWithText("Start a race").assertIsDisplayed()

        compose.onNodeWithTag("home_mode_solo").performClick().assertIsSelected()
        compose.onNodeWithTag("daily_challenge").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("daily_seven_day_summary", useUnmergedTree = true).assertExists()
    }

    @Test fun welcomingMessageAndModeControlsRemainReadableAtTwoHundredPercentOnNarrowPhones() {
        compose.setContent {
            SudokuTogetherTheme {
                CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, 2f)) {
                    Box(Modifier.width(320.dp).fillMaxHeight()) {
                        SudokuApp(GameUiState(), polishActions(), {})
                    }
                }
            }
        }
        compose.onNodeWithTag("home_game_banner").performScrollTo().assertIsDisplayed()
        val message = compose.onNodeWithText("Good puzzles.\nGood company.").assertIsDisplayed()
        val layouts = mutableListOf<TextLayoutResult>()
        message.performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(layouts) }
        assertFalse("Welcome banner must not clip at 200% font size", layouts.single().hasVisualOverflow)
        compose.onNodeWithTag("home_mode_together").performScrollTo().assertIsDisplayed().performClick()
        compose.onNodeWithText("Shared board").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("daily_challenge").performScrollTo().assertIsDisplayed()
    }
}

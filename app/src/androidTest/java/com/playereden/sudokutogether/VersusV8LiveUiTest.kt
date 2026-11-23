package com.playereden.sudokutogether

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/** V8-A is an isolated visual comparison, NOT approved production art. */
class VersusV8LiveUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun canonicalFoxOwlAndTwoGenuineBoardsShowWithoutV7Ghosts() {
        compose.setContent {
            SudokuTogetherTheme { SudokuApp(GameUiState(), polishActions(), {}) }
        }
        compose.onNodeWithTag("home_mode_versus").performScrollTo().performClick()
        compose.onNodeWithTag("nearby_art_versus").assertIsDisplayed()
        compose.onNodeWithTag("versus_v8_live_art", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("versus_v8_fox", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("versus_v8_owl", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("versus_v8_board_fox", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("versus_v8_board_owl", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("nearby_art_v7", useUnmergedTree = true).assertDoesNotExist()
        compose.onNodeWithTag("nearby_art_motif", useUnmergedTree = true).assertDoesNotExist()
        val banner = compose.onNodeWithTag("nearby_art_versus").getUnclippedBoundsInRoot()
        val heading = compose.onNodeWithText("Race to finish").getUnclippedBoundsInRoot()
        assertTrue("Text must stay inside left-half title safe zone",
            heading.right <= banner.left + (banner.right - banner.left) * .46f)
        compose.onNodeWithText("Start a race").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Join a game").performScrollTo().assertIsDisplayed()
    }

    @Test fun coOpStillHasItsOriginalIllustratedAssetAndNativeActions() {
        compose.setContent { SudokuTogetherTheme { SudokuApp(GameUiState(), polishActions(), {}) } }
        compose.onNodeWithTag("home_mode_together").performScrollTo().performClick()
        compose.onNodeWithTag("nearby_art_v5", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("versus_v8_live_art", useUnmergedTree = true).assertDoesNotExist()
        compose.onNodeWithText("Shared board").assertIsDisplayed()
        compose.onNodeWithText("Start a game").performScrollTo().assertIsDisplayed()
    }
}

package com.playereden.sudokutogether

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/** Alternative V8-B art reviewer: only TWO original perspective boards in the bitmap. */
class VersusV8BNaturalUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun twoCanonicalMascotsSitAtOriginalBoardStationsWithoutExtraGrids() {
        compose.setContent { SudokuTogetherTheme { SudokuApp(GameUiState(), polishActions(), {}) } }
        compose.onNodeWithTag("home_mode_versus").performScrollTo().performClick()
        compose.onNodeWithTag("nearby_art_versus").assertIsDisplayed()
        compose.onNodeWithTag("versus_v8b_natural_art", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("versus_v8b_fox", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("versus_v8b_owl", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("nearby_art_v7", useUnmergedTree = true).assertDoesNotExist()
        compose.onNodeWithTag("versus_v8_board_fox", useUnmergedTree = true).assertDoesNotExist()
        compose.onNodeWithTag("versus_v8_board_owl", useUnmergedTree = true).assertDoesNotExist()
        compose.onNodeWithTag("nearby_art_motif", useUnmergedTree = true).assertDoesNotExist()

        val banner = compose.onNodeWithTag("nearby_art_versus").getUnclippedBoundsInRoot()
        val title = compose.onNodeWithText("Race to finish").getUnclippedBoundsInRoot()
        assertTrue("Native title must remain left of the larger mascots",
            title.right <= banner.left + (banner.right - banner.left) * .47f)
        compose.onNodeWithText("Start a race").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Join a game").performScrollTo().assertIsDisplayed()
    }

    @Test fun originalCoopIllustrationAndActionsRemainUnchanged() {
        compose.setContent { SudokuTogetherTheme { SudokuApp(GameUiState(), polishActions(), {}) } }
        compose.onNodeWithTag("home_mode_together").performScrollTo().performClick()
        compose.onNodeWithTag("nearby_art_v5", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("versus_v8b_natural_art", useUnmergedTree = true).assertDoesNotExist()
        compose.onNodeWithText("Shared board").assertIsDisplayed()
        compose.onNodeWithText("Start a game").performScrollTo().assertIsDisplayed()
    }
}

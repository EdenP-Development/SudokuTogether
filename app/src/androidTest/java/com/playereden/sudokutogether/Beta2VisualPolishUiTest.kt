package com.playereden.sudokutogether

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/** First owner-screenshot-driven fixes without touching cosmetics, network or saved games. */
class Beta2VisualPolishUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun coopHeadingStaysInTextSafeLeftAreaAndActionsRemainReachable() {
        compose.setContent {
            SudokuTogetherTheme { SudokuApp(GameUiState(), polishActions(), {}) }
        }
        compose.onNodeWithTag("home_mode_together").performScrollTo().performClick()
        val banner = compose.onNodeWithTag("nearby_art_coop", useUnmergedTree = true)
            .getUnclippedBoundsInRoot()
        val heading = compose.onNodeWithText("Shared board")
            .getUnclippedBoundsInRoot()
        assertTrue("Title should not enter the main character half",
            heading.right <= banner.left + banner.width * .54f)
        compose.onNodeWithText("Start a game").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Join a game").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("require_code").performScrollTo().assertExists()
    }

    @Test fun collectionLongNameAndNewAchievementCopyAppearOnActualAndroidScreen() {
        compose.setContent {
            SudokuTogetherTheme {
                SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {})
            }
        }
        compose.onNodeWithTag("nav_collection").performClick()
        compose.onNodeWithText("Basic").performScrollTo().performClick()
        compose.onNodeWithTag("cosmetic_emblem_constellation").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Constellation").assertExists()
        compose.onNodeWithTag("nav_profile").performClick()
        compose.onNodeWithText("View achievements").performScrollTo().performClick()
        compose.onNodeWithText(
            "Every puzzle brings you closer to another medal. What you earn is yours to keep!"
        ).assertExists()
    }
}

package com.playereden.sudokutogether

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import com.playereden.sudokutogether.core.GameRecord
import com.playereden.sudokutogether.core.LocalPlayerData
import com.playereden.sudokutogether.core.SOLO
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/** Owner phone audit: long achievement lists must not look like endless full-size cards. */
class AchievementAlbumUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun expandedUnfinishedGoalsUseCompactIndexWithExactCounts() {
        compose.setContent {
            SudokuTogetherTheme { SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {}) }
        }
        compose.onNodeWithText("View achievements").performScrollTo().performClick()
        compose.onNodeWithTag("achievements_all_goals_toggle").performScrollTo().performClick()
        val row = compose.onNodeWithTag("achievement_solo-50")
            .performScrollTo().assertIsDisplayed()
        val bounds = row.getUnclippedBoundsInRoot()
        assertTrue("Expanded goal index should be visibly shorter than the original full-height cards",
            bounds.bottom - bounds.top < 120.dp)
        compose.onNodeWithText("Solve fifty solo puzzles").assertExists()
        compose.onNodeWithTag("earned_medal_gallery").assertDoesNotExist()
    }

    @Test fun earnedAchievementsDisplayAsMedalShelfWithoutChangingOwnership() {
        val records = (1..110).map {
            GameRecord("album-solo-$it", SOLO, "SOLVED", "p-$it", 900_000, 35, it.toLong(),
                difficulty = "EASY", assisted = true)
        }
        compose.setContent {
            SudokuTogetherTheme {
                SudokuApp(GameUiState(page = Page.PROFILE,
                    profile = LocalPlayerData(records = records)), polishActions(), {})
            }
        }
        compose.onNodeWithText("View achievements").performScrollTo().performClick()
        compose.onNodeWithTag("achievements_earned_toggle").performScrollTo().performClick()
        compose.onNodeWithTag("earned_medal_gallery").performScrollTo().assertExists()
        compose.onNodeWithTag("achievement_solo-100").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Century Club").assertExists()
        compose.onNodeWithTag("achievement_solo-100").performClick()
        compose.onNodeWithText("Earned medal · Solo").assertIsDisplayed()
        compose.onNodeWithText("This achievement is yours to keep.").assertIsDisplayed()
        compose.onNodeWithText("Close").performClick()
        compose.onNodeWithTag("achievement_solo-100").assertIsDisplayed()
    }

    @Test fun woodpeckerStaysOneLineInActualThreeColumnCollection() {
        compose.setContent {
            SudokuTogetherTheme { SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {}) }
        }
        compose.onNodeWithTag("nav_collection").performClick()
        val name = compose.onNodeWithText("Woodpecker").performScrollTo().assertExists()
        val textLayouts = mutableListOf<TextLayoutResult>()
        name.performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(textLayouts) }
        assertEquals("Woodpecker must not wrap its final letter onto a second line",
            1, textLayouts.single().lineCount)
    }
}

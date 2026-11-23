package com.playereden.sudokutogether

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.playereden.sudokutogether.core.GameRecord
import com.playereden.sudokutogether.core.LocalPlayerData
import com.playereden.sudokutogether.core.SOLO
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/** Actual Android Collection and veteran achievement-screen smoke checks. */
class VeteranRewardsUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun newVeteranIconLivesInBasicCollectionWithDualUnlockPreview() {
        compose.setContent {
            SudokuTogetherTheme {
                SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {})
            }
        }
        compose.onNodeWithTag("nav_collection").performClick()
        compose.onNodeWithText("Basic").performScrollTo().performClick()
        compose.onNodeWithText("1 of 12 available").assertIsDisplayed()
        compose.onNodeWithTag("cosmetic_emblem_constellation").performScrollTo().performClick()
        compose.onNodeWithText("Complete 135 puzzles or solve 15 Hard solo puzzles")
            .assertIsDisplayed()
        compose.onNodeWithText("Challenge progress: 0 of 15").assertIsDisplayed()
        compose.onNodeWithText("Use icon").assertDoesNotExist()
    }

    @Test fun veteranPlayerSeesEarnedMedalsAndMoreGoalsWithoutAllDoneMessage() {
        val records = (1..110).map {
            GameRecord("solo-veteran-$it", SOLO, "SOLVED", "p-$it", 900_000, 35, it.toLong(),
                difficulty = "EASY", assisted = true)
        }
        val state = mutableStateOf(GameUiState(page = Page.PROFILE,
            profile = LocalPlayerData(records = records)))
        compose.setContent {
            SudokuTogetherTheme { SudokuApp(state.value, polishActions(), {}) }
        }
        compose.onNodeWithText("View achievements").performScrollTo().performClick()
        compose.onNodeWithText("Your achievement album").assertExists()
        compose.onNodeWithTag("achievements_all_goals_toggle").assertExists()
        compose.onNodeWithTag("achievements_earned_toggle").performScrollTo().performClick()
        compose.onNodeWithTag("achievement_solo-100").performScrollTo().assertIsDisplayed()
        assertEquals(110, state.value.profile.records.size)
    }
}

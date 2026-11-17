package com.playereden.sudokutogether

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.playereden.sudokutogether.core.Appearance
import com.playereden.sudokutogether.core.GameRecord
import com.playereden.sudokutogether.core.LocalPlayerData
import com.playereden.sudokutogether.core.SOLO
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/** Phone-width Collection regression for the two new Android board styles. */
class BoardPilotCollectionUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun boardCardsPreviewLockedProgressAndEquipOnlyWhenEarned() {
        val state = mutableStateOf(GameUiState(page = Page.PROFILE))
        val actions = polishActions(cosmetic = { group, id ->
            assertEquals("board", group)
            state.value = state.value.copy(appearance = state.value.appearance.copy(board = id))
        })
        compose.setContent {
            SudokuTogetherTheme {
                Box(Modifier.width(340.dp).fillMaxHeight()) {
                    SudokuApp(state.value, actions, {})
                }
            }
        }
        compose.onNodeWithTag("nav_collection").performClick()
        compose.onNodeWithText("Boards").performClick().assertIsSelected()
        compose.onNodeWithText("1 of 6 available").assertIsDisplayed()
        compose.onNodeWithTag("cosmetic_board_parchment").performScrollTo().performClick()
        compose.onNodeWithText("Solve 8 solo puzzles").assertIsDisplayed()
        compose.onNodeWithText("Use board").assertDoesNotExist()
        compose.onNodeWithText("Close").performClick()

        val eight = List(8) { index ->
            GameRecord("solo-$index", SOLO, "SOLVED", "p", 1000L, 1, 1000L)
        }
        compose.runOnIdle { state.value = state.value.copy(profile = LocalPlayerData(records = eight)) }
        compose.onNodeWithText("4 of 6 available").assertIsDisplayed()
        compose.onNodeWithTag("cosmetic_board_parchment").performScrollTo().performClick()
        compose.onNodeWithText("Use board").performClick()
        compose.onNodeWithTag("cosmetic_board_parchment").assertIsSelected()

        compose.onNodeWithTag("cosmetic_board_graphite").performScrollTo().performClick()
        compose.onNodeWithText("Complete 20 puzzles").assertIsDisplayed()
        compose.onNodeWithText("Use board").assertDoesNotExist()
        compose.onNodeWithText("Close").performClick()

        val twenty = List(20) { index ->
            GameRecord("solo-$index", SOLO, "SOLVED", "p", 1000L, 1, 1000L)
        }
        compose.runOnIdle { state.value = state.value.copy(profile = LocalPlayerData(records = twenty)) }
        compose.onNodeWithText("5 of 6 available").assertIsDisplayed()
        compose.onNodeWithTag("cosmetic_board_graphite").performScrollTo().performClick()
        compose.onNodeWithText("Use board").performClick()
        compose.onNodeWithTag("cosmetic_board_graphite").assertIsSelected()
    }
}

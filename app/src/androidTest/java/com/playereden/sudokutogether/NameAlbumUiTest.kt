package com.playereden.sudokutogether

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.playereden.sudokutogether.core.LocalPlayerData
import org.junit.Rule
import org.junit.Test

class NameAlbumUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun journalAlbumFiltersWordsAndReflectsDurableAchievements() {
        val state = mutableStateOf(GameUiState(page = Page.PROFILE, name = "Happy Fox"))
        compose.setContent { SudokuTogetherTheme { SudokuApp(state.value, polishActions(), {}) } }
        compose.onNodeWithText("Name Collection").performScrollTo().performClick()
        compose.onNodeWithText("30 of 171 words collected").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("name_album_filter_available").performScrollTo().performClick()
        compose.onNodeWithTag("name_album_word_first.happy").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("name_album_word_first.first").assertDoesNotExist()
        compose.runOnIdle {
            state.value = state.value.copy(profile = LocalPlayerData(earnedAchievementIds = listOf("first-grid")))
        }
        compose.onNodeWithText("33 of 171 words collected").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("name_album_word_first.first").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("name_album_filter_locked").performScrollTo().performClick()
        compose.onNodeWithTag("name_album_word_first.first").assertDoesNotExist()
        compose.onNodeWithTag("name_album_word_first.patient").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("name_album_slot_middle").performScrollTo().performClick()
        compose.onNodeWithTag("name_album_word_middle.fern").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("name_album_word_first.patient").assertDoesNotExist()
        compose.onNodeWithText("Build your name").performScrollTo().performClick()
        compose.onNodeWithTag("name_preview").assertTextEquals("Happy Fox")
        compose.onNodeWithText("Cancel").performClick()
        compose.onNodeWithContentDescription("Back to Profile").performScrollTo().performClick()
        compose.onNodeWithTag("profile_player_card").performScrollTo().assertIsDisplayed()
    }
}

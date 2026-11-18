package com.playereden.sudokutogether

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.playereden.sudokutogether.core.Appearance
import com.playereden.sudokutogether.core.GameRecord
import com.playereden.sudokutogether.core.LocalPlayerData
import com.playereden.sudokutogether.core.SOLO
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

class ProfileNavigationUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun homeAvatarOpensProfileWithReachableRecordsAndAchievementsAtLargeText() {
        val name = "Alexandria Puzzle Friend"
        val state = mutableStateOf(GameUiState(name = name))
        val actions = polishActions().copy(profile = { state.value = state.value.copy(page = Page.PROFILE) })
        compose.setContent {
            SudokuTogetherTheme {
                CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, 2f)) {
                    Box(Modifier.width(320.dp).fillMaxHeight()) { SudokuApp(state.value, actions, {}) }
                }
            }
        }

        compose.onNodeWithContentDescription("My profile & records").performClick()
        compose.onNodeWithTag("nav_records").assertIsSelected().assertContentDescriptionEquals("Profile")
        val layout = mutableListOf<TextLayoutResult>()
        compose.onNodeWithText(name).performScrollTo().assertIsDisplayed()
            .performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(layout) }
        assertFalse(layout.single().hasVisualOverflow)
        compose.onNodeWithText("Game history").performScrollTo().performClick()
        compose.onNodeWithText("DAILY HISTORY").performScrollTo().assertIsDisplayed()
        compose.onNodeWithContentDescription("Back to Profile").performScrollTo().performClick()
        compose.onNodeWithText("View achievements").performScrollTo().performClick()
        compose.onNodeWithText("First Grid").performScrollTo().assertIsDisplayed()
    }

    @Test fun equippedPortraitOpensCollectionAndShowsCurrentLookWithoutExtraNavigation() {
        val state = mutableStateOf(GameUiState(page = Page.PROFILE, name = "Puzzle Partner",
            appearance = Appearance(portrait = "woodland_owl")))
        compose.setContent { SudokuTogetherTheme { SudokuApp(state.value, polishActions(), {}) } }

        compose.onNodeWithTag("profile_icon_quick_change")
            .assertIsDisplayed().assertContentDescriptionEquals("Change player icon").performClick()
        compose.onNodeWithTag("nav_collection").assertIsSelected()
        compose.onNodeWithText("EQUIPPED").assertIsDisplayed()
        compose.onNodeWithText("Owl · Woodland").assertIsDisplayed()
        compose.onNodeWithTag("collection_current_style_preview").performClick()
        compose.onNodeWithText("Your style").assertIsDisplayed()
        compose.onNodeWithText("Close").performClick()
        compose.onNodeWithText("Boards").performClick()
        compose.onNodeWithText("Classic · Board").assertIsDisplayed()
    }

    @Test fun largeFontLeavesTheAvatarAndFullNameReachable() {
        val name = "Alexandria Puzzle Friend"
        compose.setContent {
            SudokuTogetherTheme {
                CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, 2f)) {
                    Box(Modifier.width(320.dp).fillMaxHeight()) {
                        SudokuApp(GameUiState(page = Page.PROFILE, name = name), polishActions(), {})
                    }
                }
            }
        }
        compose.onNodeWithTag("profile_icon_quick_change").performScrollTo().assertIsDisplayed()
        val result = mutableListOf<TextLayoutResult>()
        compose.onNodeWithText(name).performScrollTo().assertIsDisplayed()
            .performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(result) }
        assertFalse("Profile name must remain readable at 200% font size", result.single().hasVisualOverflow)
        compose.onNodeWithContentDescription("Edit name").performScrollTo().assertIsDisplayed()
    }

    @Test fun achievementSpotlightKeepsTheTitleReadableAtLargeTextOnANarrowPhone() {
        compose.setContent {
            SudokuTogetherTheme {
                CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, 2f)) {
                    Box(Modifier.width(320.dp).fillMaxHeight()) {
                        SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {})
                    }
                }
            }
        }
        compose.onNodeWithText("0 of 9 earned").performScrollTo().assertIsDisplayed()
        val title = compose.onNodeWithText("First Grid").performScrollTo().assertIsDisplayed()
        val layouts = mutableListOf<TextLayoutResult>()
        title.performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(layouts) }
        assertFalse("Achievement title must never clip at large font scale", layouts.single().hasVisualOverflow)
        compose.onNodeWithText("0 of 1").performScrollTo().assertIsDisplayed()
    }

    @Test fun nextAchievementReflectsStoredPuzzleCompletions() {
        val state = mutableStateOf(GameUiState(page = Page.PROFILE))
        compose.setContent { SudokuTogetherTheme { SudokuApp(state.value, polishActions(), {}) } }
        compose.onNodeWithTag("profile_next_achievement").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("NEXT MILESTONE").assertIsDisplayed()
        compose.onNodeWithText("0 of 9 earned").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("First Grid").assertIsDisplayed()
        compose.onNodeWithText("0 of 1").assertIsDisplayed()

        compose.runOnIdle {
            val solved = GameRecord("finished-solo", SOLO, "SOLVED", "EASY", 650000L, 0, 123L)
            state.value = state.value.copy(profile = LocalPlayerData(records = listOf(solved)))
        }
        compose.onNodeWithText("Steady Hand").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("First Grid").assertDoesNotExist()
    }
}

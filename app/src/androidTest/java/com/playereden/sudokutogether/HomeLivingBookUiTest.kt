package com.playereden.sudokutogether

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

/** Home gamefront regression: actual actions stay accessible behind original artwork. */
class HomeLivingBookUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun soloDailyAndNearbyGamesRemainDiscoverable() {
        compose.setContent { SudokuTogetherTheme {
            SudokuApp(GameUiState(), polishActions(), {})
        } }
        compose.onNodeWithTag("home_book_stage").assertIsDisplayed()
        compose.onNodeWithTag("home_chapter_art").assertIsDisplayed()
        compose.onNodeWithTag("home_woodland_backdrop", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("home_portrait_default_fox", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("home_chapter_featured", useUnmergedTree = true).assertExists()
        compose.onNodeWithText("Play Sudoku").assertIsDisplayed()
        compose.onNodeWithText("New puzzle").assertIsDisplayed()
        compose.onNodeWithTag("difficulty_EASY").assertExists()
        compose.onNodeWithTag("daily_challenge").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("daily_seven_day_history", useUnmergedTree=true).assertExists()
        compose.onNodeWithText("past 7 days").assertExists()
        // Distinguish Tuesday/Thursday and Saturday/Sunday in the ritual row.
        compose.onNodeWithTag("daily_weekday_Tu", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("daily_weekday_Th", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("nav_book_binding", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("nav_play").assertIsSelected()

        compose.onNodeWithTag("home_mode_together").performScrollTo().performClick()
        compose.onNodeWithTag("nearby_art_coop", useUnmergedTree = true).assertIsDisplayed()
        compose.onNodeWithTag("nearby_art_image", useUnmergedTree = true).assertExists()
        assertModeArtDoesNotDuplicateMascots()
        compose.onNodeWithText("Shared board").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Start a game").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Join a game").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("require_code").performScrollTo().assertExists()

        compose.onNodeWithTag("home_mode_versus").performScrollTo().performClick()
        compose.onNodeWithTag("nearby_setup").assertIsDisplayed()
        compose.onNodeWithTag("nearby_art_versus", useUnmergedTree = true).assertIsDisplayed()
        compose.onNodeWithTag("nearby_art_v6", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("nearby_art_v5", useUnmergedTree = true).assertDoesNotExist()
        assertModeArtDoesNotDuplicateMascots()
        compose.onNodeWithTag("nearby_art_coop", useUnmergedTree = true).assertDoesNotExist()
        compose.onNodeWithText("Race to finish").assertIsDisplayed()
        compose.onNodeWithText("Start a race").performScrollTo().assertIsDisplayed()
        // Nearby is a dedicated setup screen; switching back restores the Daily card.
        compose.onNodeWithTag("daily_challenge").assertDoesNotExist()
        compose.onNodeWithTag("home_mode_solo").performScrollTo().performClick()
        compose.onNodeWithTag("home_book_stage").assertIsDisplayed()
        compose.onNodeWithTag("daily_challenge").assertExists()
    }

    @Test fun illustratedNearbySetupRetainsAccessibleActionsAtLargeText() {
        compose.setContent { SudokuTogetherTheme {
            CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, 2f)) {
                Box(Modifier.width(320.dp).fillMaxHeight()) {
                    SudokuApp(GameUiState(), polishActions(), {})
                }
            }
        } }
        compose.onNodeWithTag("home_mode_together").performScrollTo().performClick()
        compose.onNodeWithTag("nearby_art_coop", useUnmergedTree = true).assertExists()
        compose.onNodeWithText("Shared board").assertExists()
        assertHeadingFits("Shared board")
        compose.onNodeWithTag("nearby_difficulty_MEDIUM").performScrollTo().assertExists()
        compose.onNodeWithTag("require_code").performScrollTo().assertExists()
        compose.onNodeWithText("Start a game").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Join a game").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("home_mode_versus").performScrollTo().performClick()
        compose.onNodeWithTag("nearby_art_versus", useUnmergedTree = true).assertExists()
        assertHeadingFits("Race to finish")
        compose.onNodeWithText("Start a race").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Join a game").performScrollTo().assertIsDisplayed()
    }

    /**
     * Both Co-op V5 and Versus V6 contain their two mascots already. The V4
     * fallback draws the approved Collection portraits separately. Never
     * duplicate characters in either banner or lose the V4 fallback.
     */
    private fun assertModeArtDoesNotDuplicateMascots() {
        val embeddedV5 = compose.onAllNodesWithTag("nearby_art_v5", useUnmergedTree = true)
            .fetchSemanticsNodes().isNotEmpty()
        val embeddedV6 = compose.onAllNodesWithTag("nearby_art_v6", useUnmergedTree = true)
            .fetchSemanticsNodes().isNotEmpty()
        assertFalse("Only one complete banner can be active", embeddedV5 && embeddedV6)
        if (embeddedV5 || embeddedV6) {
            compose.onNodeWithTag("nearby_art_motif", useUnmergedTree = true).assertDoesNotExist()
            compose.onNodeWithTag("nearby_art_fox", useUnmergedTree = true).assertDoesNotExist()
            compose.onNodeWithTag("nearby_art_owl", useUnmergedTree = true).assertDoesNotExist()
        } else {
            compose.onNodeWithTag("nearby_art_motif", useUnmergedTree = true).assertExists()
            compose.onNodeWithTag("nearby_art_fox", useUnmergedTree = true).assertExists()
            compose.onNodeWithTag("nearby_art_owl", useUnmergedTree = true).assertExists()
        }
    }

    private fun assertHeadingFits(heading: String) {
        val layouts = mutableListOf<TextLayoutResult>()
        compose.onNodeWithText(heading).performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(layouts) }
        val layout = layouts.single()
        assertFalse("Nearby mode title must not visually overflow at 320dp/200% text: $heading", layout.hasVisualOverflow)
    }

    @Test fun homeSpotlightShowsTheActuallySelectedCollectionPortrait() {
        compose.setContent { SudokuTogetherTheme {
            SudokuApp(GameUiState(appearance = com.playereden.sudokutogether.core.Appearance(
                portrait = "ocean_octopus"
            )), polishActions(), {})
        } }
        compose.onNodeWithTag("home_chapter_art").assertIsDisplayed()
        compose.onNodeWithText("With your ocean companion").assertIsDisplayed()
        compose.onNodeWithTag("home_woodland_backdrop", useUnmergedTree = true).assertDoesNotExist()
        compose.onNodeWithTag("home_portrait_ocean_octopus", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("home_chapter_owl", useUnmergedTree = true).assertDoesNotExist()
        compose.onNodeWithTag("home_chapter_featured", useUnmergedTree = true).assertExists()
        compose.onNodeWithTag("daily_challenge").assertExists()
    }

    @Test fun bookMainActionStaysReadableOnNarrowLargeTextPhones() {
        compose.setContent { SudokuTogetherTheme {
            CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, 2f)) {
                Box(Modifier.width(320.dp).fillMaxHeight()) {
                    SudokuApp(GameUiState(), polishActions(), {})
                }
            }
        } }
        compose.onNodeWithTag("home_book_stage").performScrollTo().assertIsDisplayed()
        val title = compose.onNodeWithText("Play Sudoku").assertIsDisplayed()
        val result = mutableListOf<TextLayoutResult>()
        title.performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(result) }
        val measured = result.single()
        assertFalse("Book title must be readable at 200% font: size=${measured.size}, lines=${measured.lineCount}, didOverflowWidth=${measured.didOverflowWidth}, didOverflowHeight=${measured.didOverflowHeight}", measured.hasVisualOverflow)
        compose.onNodeWithText("New puzzle").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("daily_challenge").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("home_mode_together").performScrollTo().assertIsDisplayed()
    }
}

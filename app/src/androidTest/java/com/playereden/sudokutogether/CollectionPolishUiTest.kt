package com.playereden.sudokutogether

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.playereden.sudokutogether.core.Appearance
import com.playereden.sudokutogether.core.COOP
import com.playereden.sudokutogether.core.GameRecord
import com.playereden.sudokutogether.core.LocalPlayerData
import com.playereden.sudokutogether.core.VERSUS
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CollectionPolishUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun compactProfileActionsEditTheNameAndOpenPlayerIconsAtLargeText() {
        val state = mutableStateOf(GameUiState(page = Page.PROFILE, name = "Alexandria Puzzle Friend"))
        val actions = polishActions().copy(name = { state.value = state.value.copy(name = it) })
        compose.setContent {
            SudokuTogetherTheme {
                CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, 2f)) {
                    Box(Modifier.width(320.dp).fillMaxHeight()) { SudokuApp(state.value, actions, {}) }
                }
            }
        }

        val edit = compose.onNodeWithContentDescription("Edit name").assertIsDisplayed()
        val editBounds = edit.getUnclippedBoundsInRoot()
        val editWidth = editBounds.right - editBounds.left
        // The pixel-to-dp conversion can report exactly 48dp as 47.999985dp.
        // Tolerate numerical precision only, never an appreciably smaller target.
        assertTrue("Edit name target must be 48dp; actual=$editWidth, bounds=$editBounds",
            editWidth + 0.001.dp >= 48.dp)
        edit.performClick()
        compose.onNodeWithTag("player_name").performTextReplacement("Puzzle partner")
        compose.onNodeWithText("Save").performClick()
        compose.onNodeWithText("Puzzle partner").assertIsDisplayed()

        compose.onNodeWithText("Change icon").performScrollTo().performClick()
        compose.onNodeWithTag("nav_collection").assertIsSelected()
        compose.onNodeWithText("Player icons").assertIsSelected()
        compose.onNodeWithTag("cosmetic_emblem_tile").performScrollTo().assertIsDisplayed()
    }

    @Test fun equippedAndAvailableIconsAppearAheadOfLockedIcons() {
        val data = LocalPlayerData(records = listOf(GameRecord("race", VERSUS, "WIN", "p", 1000, 1, 1000)))
        val state = mutableStateOf(GameUiState(page = Page.PROFILE, profile = data, appearance = Appearance(portrait = "woodland_rabbit")))
        compose.setContent { SudokuTogetherTheme { SudokuApp(state.value, polishActions(), {}) } }

        compose.onNodeWithTag("nav_collection").performClick()
        compose.onNodeWithText("7 of 28 available").assertIsDisplayed()
        val equipped = compose.onNodeWithTag("portrait_woodland_rabbit").assertIsSelected()
        val starter = compose.onNodeWithTag("cosmetic_emblem_tile")
        val lastAvailable = compose.onNodeWithTag("portrait_garden_honeybee")
        val firstLocked = compose.onNodeWithTag("cosmetic_emblem_leaf")
        val equippedBounds = equipped.getUnclippedBoundsInRoot()
        val starterBounds = starter.getUnclippedBoundsInRoot()
        assertTrue(
            "The equipped icon should come first in the gallery",
            equippedBounds.top < starterBounds.top ||
                (equippedBounds.top == starterBounds.top && equippedBounds.left < starterBounds.left)
        )
        assertTrue("Locked icons should be below the available icons",
            lastAvailable.getUnclippedBoundsInRoot().bottom <= firstLocked.getUnclippedBoundsInRoot().top)

        compose.runOnIdle { state.value = state.value.copy(profile = data.copy(records = emptyList())) }
        compose.onNodeWithText("6 of 28 available").assertIsDisplayed()
        equipped.assertIsSelected().assertIsDisplayed()
        assertTrue(equipped.getUnclippedBoundsInRoot().bottom <= firstLocked.getUnclippedBoundsInRoot().top)
    }

    @Test fun numbersLetsPlayersBrowseFontsSeparatelyFromColors() {
        compose.setContent { SudokuTogetherTheme { SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {}) } }

        compose.onNodeWithTag("nav_collection").performClick()
        compose.onNodeWithText("Numbers").performClick()
        compose.onNodeWithText("Number colors").assertIsSelected()
        compose.onNodeWithTag("cosmetic_ink_forest").assertIsSelected()
        compose.onNodeWithTag("cosmetic_font_clear").assertDoesNotExist()
        compose.onNodeWithText("Number fonts").performClick().assertIsSelected()
        compose.onNodeWithTag("cosmetic_font_clear").assertIsSelected()
        compose.onNodeWithTag("cosmetic_ink_forest").assertDoesNotExist()
    }

    @Test fun currentStylePreviewFollowsTheCollectionTab() {
        val state = mutableStateOf(GameUiState(page = Page.PROFILE))
        compose.setContent { SudokuTogetherTheme { SudokuApp(state.value, polishActions(), {}) } }
        compose.onNodeWithTag("nav_collection").performClick()

        compose.onNodeWithText("Owl · Woodland").assertIsDisplayed()
        compose.onNodeWithText("Preview your style").assertIsDisplayed()

        compose.onNodeWithText("Boards").performClick()
        compose.onNodeWithText("Classic · Board").assertIsDisplayed()
        compose.onNodeWithText("Preview your current board").assertIsDisplayed()
        compose.onNodeWithText("Owl · Woodland").assertDoesNotExist()
        compose.onNodeWithTag("collection_current_style_preview").performClick()
        compose.onNodeWithText("Your style").assertIsDisplayed()
        compose.onNodeWithText("Close").performClick()

        compose.onNodeWithText("Numbers").performClick()
        compose.onNodeWithText("Forest · Clear").assertIsDisplayed()
        compose.onNodeWithText("Preview your current numbers").assertIsDisplayed()

        compose.runOnIdle { state.value = state.value.copy(uniformNumbers = true) }
        compose.onNodeWithText("Classic number display is on", substring = true).assertExists()
        compose.onNodeWithText("Forest · Clear").assertIsDisplayed()
    }

    @Test fun iconFamiliesStayOnOneRowAndCanBeSwipedBothWays() {
        compose.setContent {
            SudokuTogetherTheme {
                Box(Modifier.width(320.dp).fillMaxHeight()) {
                    SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {})
                }
            }
        }

        compose.onNodeWithTag("nav_collection").performClick()
        val all = compose.onNodeWithText("All").assertIsSelected()
        val garden = compose.onNodeWithText("Garden")
        assertEquals(
            "The last icon set belongs in the same row as the first",
            all.getUnclippedBoundsInRoot().top,
            garden.getUnclippedBoundsInRoot().top
        )

        val picker = compose.onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.HorizontalScrollAxisRange))
        compose.onNodeWithTag("collection_family_scroll_hint").assertIsDisplayed()
        val viewport = picker.getUnclippedBoundsInRoot()
        assertTrue(garden.getUnclippedBoundsInRoot().right > viewport.right)
        picker.performTouchInput { swipeLeft() }
        assertTrue(garden.getUnclippedBoundsInRoot().right <= viewport.right)
        all.assertIsSelected()
        garden.assertIsDisplayed().performClick().assertIsSelected()
        compose.onNodeWithText("1 of 4 available").assertIsDisplayed()
        compose.onNodeWithTag("portrait_garden_honeybee").assertIsDisplayed()
        compose.onNodeWithTag("portrait_ocean_octopus").assertDoesNotExist()

        compose.onNodeWithText("Boards").performClick()
        compose.onNodeWithText("Player icons").performClick()
        garden.assertIsDisplayed().assertIsSelected()
        assertTrue(garden.getUnclippedBoundsInRoot().right <= viewport.right)

        picker.performTouchInput { swipeRight() }
        assertTrue(all.getUnclippedBoundsInRoot().left >= viewport.left)
        all.assertIsDisplayed().performClick().assertIsSelected()
        compose.onNodeWithTag("cosmetic_emblem_tile").assertIsDisplayed()
        compose.onNodeWithTag("portrait_ocean_octopus").assertExists()
    }

    @Test fun iconFamilyHintDoesNotWasteSpaceWhenAllOptionsFit() {
        compose.setContent {
            SudokuTogetherTheme {
                Box(Modifier.width(600.dp).fillMaxHeight()) {
                    SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {})
                }
            }
        }
        compose.onNodeWithTag("nav_collection").performClick()
        compose.onNodeWithText("All").assertIsSelected()
        compose.onNodeWithText("Garden").assertIsDisplayed()
        compose.onNodeWithTag("collection_family_scroll_hint").assertDoesNotExist()
    }

    @Test fun gardenFilterShowsExistingProgressAndEquipsTheHoneybee() {
        val records = List(3) { GameRecord("coop-$it", COOP, "SOLVED", "p", 1000, 1, 1000) }
        val data = LocalPlayerData(records = records)
        val state = mutableStateOf(GameUiState(page = Page.PROFILE, profile = data))
        val actions = polishActions(cosmetic = { group, id ->
            assertEquals("portrait", group)
            state.value = state.value.copy(appearance = state.value.appearance.copy(portrait = id))
        })
        compose.setContent { SudokuTogetherTheme { SudokuApp(state.value, actions, {}) } }

        compose.onNodeWithTag("nav_collection").performClick()
        compose.onNodeWithText("Garden").performScrollTo().performClick()
        compose.onNodeWithText("1 of 4 available").assertIsDisplayed()
        compose.onNodeWithTag("portrait_ocean_octopus").assertDoesNotExist()
        compose.onNodeWithTag("portrait_garden_ladybug").performScrollTo().performClick()
        compose.onNodeWithText("Complete 4 puzzles").assertIsDisplayed()
        compose.onNodeWithText("3 of 4 completed").assertIsDisplayed()
        compose.onNodeWithText("Use icon").assertDoesNotExist()
        compose.onNodeWithText("Close").performClick()

        compose.onNodeWithTag("portrait_garden_honeybee").performScrollTo().performClick()
        compose.onNodeWithText("Use icon").performClick()
        compose.onNodeWithTag("portrait_garden_honeybee").assertIsSelected().assertIsDisplayed()
        compose.onNodeWithText("Honeybee · Garden").assertIsDisplayed()
    }
}

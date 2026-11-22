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

    @Test fun themedChaptersKeepStarterEquippedAndLockedItemsTogetherWithoutDuplication() {
        val data = LocalPlayerData(records = listOf(GameRecord("race", VERSUS, "WIN", "p", 1000, 1, 1000)))
        val state = mutableStateOf(GameUiState(page = Page.PROFILE, profile = data,
            appearance = Appearance(portrait = "woodland_rabbit")))
        compose.setContent { SudokuTogetherTheme { SudokuApp(state.value, polishActions(), {}) } }

        compose.onNodeWithTag("nav_collection").performClick()
        compose.onNodeWithText("8 of 49 available").assertIsDisplayed()
        for (chapter in 1..5) compose.onNodeWithTag("collection_chapter_$chapter").assertExists()
        for (starter in listOf("woodland_owl", "woodland_fox", "ocean_octopus",
            "garden_honeybee", "farm_cow")) {
            compose.onAllNodesWithTag("portrait_$starter").assertCountEquals(1)
        }
        compose.onNodeWithTag("portrait_woodland_rabbit").assertIsSelected()
        compose.onAllNodesWithTag("portrait_woodland_rabbit").assertCountEquals(1)
        compose.onNodeWithTag("portrait_woodland_skunk").assertExists()
        compose.onNodeWithTag("portrait_farm_chicken").assertExists()

        compose.runOnIdle {
            state.value = state.value.copy(profile = data.copy(records = emptyList()))
        }
        compose.onNodeWithText("7 of 49 available").assertIsDisplayed()
        compose.onNodeWithTag("portrait_woodland_rabbit").assertIsSelected()
        compose.onAllNodesWithTag("portrait_farm_cow").assertCountEquals(1)
    }

    @Test fun newFarmFamilyCanBeBrowsedWithCowStarterAndLockedChicken() {
        compose.setContent {
            SudokuTogetherTheme {
                SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {})
            }
        }
        compose.onNodeWithTag("nav_collection").performClick()
        compose.onNodeWithText("Farm").performScrollTo().performClick()
        compose.onNodeWithTag("portrait_farm_cow").assertExists()
        compose.onNodeWithTag("portrait_farm_chicken").assertExists()
        compose.onNodeWithTag("portrait_ocean_sea_turtle").assertDoesNotExist()
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
        // The installed phone review has Owl equipped; bare GameUiState defaults to Gridkeeper.
        val state = mutableStateOf(GameUiState(page = Page.PROFILE,
            appearance = Appearance(portrait = "woodland_owl")))
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
        compose.onNodeWithText("Classic number display is on").assertIsDisplayed()
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
        val farm = compose.onNodeWithText("Farm")
        assertEquals(
            "The last icon set belongs in the same row as the first",
            all.getUnclippedBoundsInRoot().top,
            farm.getUnclippedBoundsInRoot().top
        )

        val picker = compose.onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.HorizontalScrollAxisRange))
        compose.onNodeWithTag("collection_family_scroll_hint").assertIsDisplayed()
        val viewport = picker.getUnclippedBoundsInRoot()
        assertTrue(farm.getUnclippedBoundsInRoot().right > viewport.right)
        picker.performTouchInput { swipeLeft() }
        farm.performScrollTo()
        assertTrue(farm.getUnclippedBoundsInRoot().right <= viewport.right)
        all.assertIsSelected()
        garden.performScrollTo().performClick().assertIsSelected()
        compose.onNodeWithText("1 of 9 available").assertIsDisplayed()
        compose.onNodeWithTag("portrait_garden_honeybee").assertIsDisplayed()
        compose.onNodeWithTag("portrait_ocean_octopus").assertDoesNotExist()

        compose.onNodeWithText("Boards").performClick()
        compose.onNodeWithText("Player icons").performClick()
        garden.performScrollTo().assertIsSelected()

        picker.performTouchInput { swipeRight() }
        assertTrue(all.getUnclippedBoundsInRoot().left >= viewport.left)
        all.assertIsDisplayed().performClick().assertIsSelected()
        compose.onNodeWithTag("cosmetic_emblem_tile").assertIsDisplayed()
        compose.onNodeWithTag("portrait_ocean_octopus").assertExists()
    }

    @Test fun iconFamilyHintAppearsWhenParentConstrainsTheRequestedWideLayout() {
        // A requested 820dp child cannot force a phone-width parent to become
        // 820dp wide. The extra Farm tab still overflows on the emulator.
        // Verify the hint reflects actual constraints, not requested width.
        compose.setContent {
            SudokuTogetherTheme {
                Box(Modifier.width(820.dp).fillMaxHeight()) {
                    SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {})
                }
            }
        }
        compose.onNodeWithTag("nav_collection").performClick()
        compose.onNodeWithText("All").assertIsSelected()
        compose.onNodeWithTag("collection_family_scroll_hint").assertIsDisplayed()
        compose.onNodeWithText("Farm").performScrollTo().performClick().assertIsSelected()
        compose.onNodeWithTag("portrait_farm_cow").assertExists()
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
        compose.onNodeWithText("1 of 9 available").assertIsDisplayed()
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

    @Test fun playerIconGalleryUsesThreeColumnsOnlyWhenThereIsRoom() {
        val width = mutableStateOf(390.dp)
        compose.setContent {
            SudokuTogetherTheme {
                Box(Modifier.width(width.value).fillMaxHeight()) {
                    SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {})
                }
            }
        }
        compose.onNodeWithTag("nav_collection").performClick()
        val leaf = compose.onNodeWithTag("cosmetic_emblem_leaf")
        val duo = compose.onNodeWithTag("cosmetic_emblem_duo")
        val flash = compose.onNodeWithTag("cosmetic_emblem_flash")
        leaf.performScrollTo()
        assertEquals(leaf.getUnclippedBoundsInRoot().top, duo.getUnclippedBoundsInRoot().top)
        assertEquals(duo.getUnclippedBoundsInRoot().top, flash.getUnclippedBoundsInRoot().top)

        compose.runOnIdle { width.value = 320.dp }
        leaf.performScrollTo()
        assertEquals(leaf.getUnclippedBoundsInRoot().top, duo.getUnclippedBoundsInRoot().top)
        assertTrue("The third icon must wrap on a narrow screen",
            flash.getUnclippedBoundsInRoot().top > duo.getUnclippedBoundsInRoot().top)
    }

    @Test fun nextUnlockUsesRealProgressAndOpensExistingLockedPreview() {
        compose.setContent { SudokuTogetherTheme { SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {}) } }
        compose.onNodeWithTag("nav_collection").performClick()
        val next = compose.onNodeWithTag("collection_next_unlock").performScrollTo().assertIsDisplayed()
        // Organizing the album never hides an item or changes its durable unlock.
        compose.onNodeWithTag("collection_chapter_1").assertExists()
        compose.onNodeWithTag("collection_chapter_2").assertExists()
        compose.onNodeWithTag("collection_chapter_3").assertExists()
        compose.onNodeWithTag("collection_chapter_4").assertExists()
        compose.onNodeWithTag("portrait_ocean_octopus").assertExists()
        compose.onNodeWithText("NEXT TO UNLOCK").assertIsDisplayed()
        compose.onNodeWithText("0 of 1 completed").assertIsDisplayed()
        next.performClick()
        // Goal appears in the backdrop spotlight and in the preview dialog.
        // Only assert the active, topmost dialog's matching label.
        compose.onAllNodesWithText("Solve 1 solo puzzle").onLast().assertIsDisplayed()
        compose.onNodeWithText("Use icon").assertDoesNotExist()
        compose.onNodeWithText("Close").performClick()
    }
    @Test fun gardenMixedThemeShowsPlantsAndObjectsBesideInsects() {
        compose.setContent {
            SudokuTogetherTheme {
                SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {})
            }
        }
        compose.onNodeWithTag("nav_collection").performClick()
        compose.onNodeWithText("Garden").performScrollTo().performClick().assertIsSelected()
        compose.onNodeWithText("1 of 9 available").assertIsDisplayed()
        compose.onNodeWithTag("portrait_garden_honeybee").assertExists()
        compose.onNodeWithTag("portrait_garden_sunflower").assertExists()
        compose.onNodeWithTag("portrait_garden_strawberry").assertExists()
        compose.onNodeWithTag("portrait_garden_watering_can").assertExists()
        compose.onNodeWithTag("portrait_garden_potted_tulip").assertExists()
        compose.onNodeWithTag("portrait_ocean_octopus").assertDoesNotExist()

        compose.onNodeWithTag("portrait_garden_sunflower").performScrollTo().performClick()
        compose.onNodeWithText("Complete 11 puzzles").assertIsDisplayed()
        compose.onNodeWithText("Use icon").assertDoesNotExist()
        compose.onNodeWithText("Close").performClick()

        compose.onNodeWithTag("portrait_garden_watering_can").performScrollTo().performClick()
        compose.onNodeWithText("Complete 62 puzzles or complete 5 different Daily puzzles").assertIsDisplayed()
        compose.onNodeWithText("Challenge progress: 0 of 5").assertIsDisplayed()
        compose.onNodeWithText("Use icon").assertDoesNotExist()
        compose.onNodeWithText("Close").performClick()
    }

    @Test fun farmMixedThemeShowsAnimalCropAndObjectUnlocksTogether() {
        compose.setContent {
            SudokuTogetherTheme {
                SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {})
            }
        }
        compose.onNodeWithTag("nav_collection").performClick()
        compose.onNodeWithText("Farm").performScrollTo().performClick().assertIsSelected()
        compose.onNodeWithText("1 of 6 available").assertIsDisplayed()
        compose.onNodeWithTag("portrait_farm_cow").assertExists()
        compose.onNodeWithTag("portrait_farm_chicken").assertExists()
        compose.onNodeWithTag("portrait_farm_sheep").assertExists()
        compose.onNodeWithTag("portrait_farm_pig").assertExists()
        compose.onNodeWithTag("portrait_farm_pumpkin").assertExists()
        compose.onNodeWithTag("portrait_farm_hay_bale").assertExists()
        compose.onNodeWithTag("portrait_garden_sunflower").assertDoesNotExist()

        compose.onNodeWithTag("portrait_farm_sheep").performScrollTo().performClick()
        compose.onNodeWithText("Complete 20 puzzles or solve 3 Medium solo puzzles").assertIsDisplayed()
        compose.onNodeWithText("Challenge progress: 0 of 3").assertIsDisplayed()
        compose.onNodeWithText("Use icon").assertDoesNotExist()
        compose.onNodeWithText("Close").performClick()

        compose.onNodeWithTag("portrait_farm_pumpkin").performScrollTo().performClick()
        compose.onNodeWithText("Complete 58 puzzles or complete 4 different Daily puzzles").assertIsDisplayed()
        compose.onNodeWithText("Challenge progress: 0 of 4").assertIsDisplayed()
        compose.onNodeWithText("Use icon").assertDoesNotExist()
        compose.onNodeWithText("Close").performClick()
    }

}

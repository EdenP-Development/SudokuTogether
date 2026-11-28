package com.playereden.sudokutogether

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.playereden.sudokutogether.core.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class NameBuilderUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun builderEditsAnApprovedNameWithoutTextInput() {
        var result: PlayerNameSelection? = null
        compose.setContent {
            SudokuTogetherTheme {
                NameBuilderDialog(LocalPlayerData(), "Happy Maple Fox", { result = it }, {})
            }
        }
        compose.onNodeWithTag("name_preview").assertTextEquals("Happy Maple Fox")
        compose.onNodeWithTag("name_first_next").performScrollTo().performClick()
        compose.onNodeWithTag("name_preview").assertTextEquals("Clever Maple Fox")
        compose.onNodeWithTag("name_middle_previous").performScrollTo().performClick()
        compose.onNodeWithTag("name_preview").assertTextEquals("Clever Fox")
        compose.onNodeWithTag("name_confirm").performClick()
        compose.runOnIdle {
            assertEquals(PlayerNameSelection("first.clever", null, "last.fox"), result)
        }
    }

    @Test fun directPickerSelectsOwnedWordsAndAllowsRemovingMiddleWord() {
        var result: PlayerNameSelection? = null
        compose.setContent {
            SudokuTogetherTheme {
                NameBuilderDialog(LocalPlayerData(), "Happy Maple Fox", { result = it }, {})
            }
        }
        val picker = compose.onNodeWithTag("name_first_picker").performScrollTo()
        val anchor = picker.getUnclippedBoundsInRoot()
        picker.performClick()
        val selectedOption = compose.onNodeWithTag("name_option_first.happy").assertIsSelected()
        val option = selectedOption.getUnclippedBoundsInRoot()
        assertTrue("Menu matches picker width", kotlin.math.abs((anchor.right - anchor.left).value -
            (option.right - option.left).value) <= 2f)
        compose.onNodeWithTag("name_option_first.clever").assertIsNotSelected().performClick()
        compose.onNodeWithTag("name_preview").assertTextEquals("Clever Maple Fox")
        compose.onNodeWithTag("name_middle_picker").performScrollTo().performClick()
        compose.onNodeWithTag("name_middle_option_none").performClick()
        compose.onNodeWithTag("name_preview").assertTextEquals("Clever Fox")
        compose.onNodeWithTag("name_confirm").performClick()
        compose.runOnIdle {
            assertEquals(PlayerNameSelection("first.clever", null, "last.fox"), result)
        }
    }

    @Test fun earnedWordsCanBeSelectedAndLargeTextRandomizerRemainsReachable() {
        val profile = LocalPlayerData(earnedAchievementIds = listOf("first-grid"))
        var result: PlayerNameSelection? = null
        compose.setContent {
            SudokuTogetherTheme {
                CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, 2f)) {
                    NameBuilderDialog(profile, "First Acorn Solver", { result = it }, {})
                }
            }
        }
        compose.onNodeWithTag("name_preview").assertTextEquals("First Acorn Solver")
        compose.onNodeWithTag("name_randomize").performScrollTo().performClick()
        compose.onNodeWithTag("name_confirm").assertIsEnabled().performClick()
        compose.runOnIdle {
            assertTrue(NameCollection.canEquip(requireNotNull(result), setOf("first-grid")))
        }
    }
}

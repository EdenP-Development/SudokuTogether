package com.playereden.sudokutogether

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

/** Actual user-facing screenshot gate: Basic album holds six visible vector samples. */
class InsigniaConceptUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun eachNewEmblemCanOpenSideBySideArtReviewWithoutChangingRewardProgress() {
        assertTrue(androidx.test.platform.app.InstrumentationRegistry.getInstrumentation()
            .targetContext.resources.getBoolean(R.bool.gridmates_insignia_preview_enabled))
        compose.setContent {
            SudokuTogetherTheme { SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {}) }
        }
        compose.onNodeWithTag("nav_collection").performClick()
        val ids = listOf("constellation","sunrise","focus","compass","moonlight","grid_master")
        ids.forEach { id ->
            compose.onNodeWithTag("cosmetic_emblem_$id")
                .performScrollTo().performClick()
            compose.onAllNodesWithTag("insignia_comparison_$id")
                .assertCountEquals(1)
            compose.onNodeWithTag("insignia_candidate_art_$id", useUnmergedTree = true).assertExists()
            compose.onNodeWithText("ART STUDY · CURRENT / PROPOSED").assertExists()
            compose.onNodeWithText("Close").performClick()
        }
    }
}

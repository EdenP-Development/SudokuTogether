package com.playereden.sudokutogether

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import com.playereden.sudokutogether.core.Appearance
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class PortraitRefinementUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun stablePortraitIdsSelectReviewArtOnlyWhenEnabled() {
        assertEquals(R.drawable.woodland_woodpecker, refinedAnimalDrawable("woodland_woodpecker", false))
        assertEquals(R.drawable.garden_firefly, refinedAnimalDrawable("garden_firefly", false))
        assertEquals(R.drawable.gm_portrait_woodpecker_b, refinedAnimalDrawable("woodland_woodpecker", true))
        assertEquals(R.drawable.gm_portrait_firefly_v2, refinedAnimalDrawable("garden_firefly", true))
        assertEquals(R.drawable.woodland_rabbit, refinedAnimalDrawable("woodland_rabbit", true))
        assertEquals(1f, refinedPortraitScale("woodland_woodpecker", true), .001f)
        assertEquals(1.26f, refinedPortraitScale("woodland_woodpecker", false), .001f)
    }

    @Test fun correctedPortraitsRenderAtAllThreeActualAndroidSizes() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertTrue(context.resources.getBoolean(R.bool.gridmates_portrait_refinement_enabled))
        compose.setContent {
            SudokuTogetherTheme {
                Column {
                    for (size in listOf(48, 62, 96)) {
                        Row {
                            for (id in listOf("woodland_woodpecker", "garden_firefly")) {
                                AnimalPortrait(Appearance(portrait = id), Modifier.size(size.dp))
                            }
                        }
                    }
                }
            }
        }
        for (id in listOf("woodland_woodpecker", "garden_firefly")) {
            val nodes = compose.onAllNodesWithTag("portrait_image_$id")
            nodes.assertCountEquals(3)
            for (i in 0..2) nodes[i].assertIsDisplayed()
        }
    }

    @Test fun woodpeckerStillOpensItsRealLockedCollectionDetail() {
        checkLockedDetail("Woodland", "woodland_woodpecker")
    }

    @Test fun fireflyStillOpensItsRealLockedCollectionDetail() {
        checkLockedDetail("Garden", "garden_firefly")
    }

    private fun checkLockedDetail(family: String, id: String) {
        compose.setContent { SudokuTogetherTheme { SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {}) } }
        compose.onNodeWithTag("nav_collection").performClick()
        compose.onNodeWithText(family).performScrollTo().performClick().assertIsSelected()
        compose.onNodeWithTag("portrait_$id").performScrollTo().performClick()
        compose.onNodeWithTag("collection_detail_art").assertIsDisplayed()
        compose.onNodeWithText("Locked").assertIsDisplayed()
        compose.onNodeWithText("Close").performClick()
    }
}

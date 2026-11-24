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
        assertEquals(R.drawable.gm_portrait_woodland_woodpecker_illustrated, refinedAnimalDrawable("woodland_woodpecker", true))
        assertEquals(1f, refinedPortraitScale("woodland_woodpecker", true), .001f)
        assertEquals(R.drawable.woodland_chipmunk, refinedAnimalDrawable("woodland_chipmunk", false))
        assertEquals(R.drawable.gm_portrait_woodland_chipmunk_illustrated, refinedAnimalDrawable("woodland_chipmunk", true))
        assertEquals(1f, refinedPortraitScale("woodland_chipmunk", true), .001f)
        assertEquals(R.drawable.woodland_beaver, refinedAnimalDrawable("woodland_beaver", false))
        assertEquals(R.drawable.gm_portrait_woodland_beaver_illustrated, refinedAnimalDrawable("woodland_beaver", true))
        assertEquals(1f, refinedPortraitScale("woodland_beaver", true), .001f)
        assertEquals(R.drawable.woodland_skunk, refinedAnimalDrawable("woodland_skunk", false))
        assertEquals(R.drawable.gm_portrait_woodland_skunk_illustrated, refinedAnimalDrawable("woodland_skunk", true))
        assertEquals(1f, refinedPortraitScale("woodland_skunk", true), .001f)
        assertEquals(R.drawable.garden_firefly, refinedAnimalDrawable("garden_firefly", false))
        assertEquals(R.drawable.gm_portrait_garden_firefly_illustrated, refinedAnimalDrawable("garden_firefly", true))
        assertEquals(1f, refinedPortraitScale("garden_firefly", true), .001f)
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
                        for (group in listOf("woodland_woodpecker", "woodland_chipmunk", "woodland_beaver", "woodland_skunk", "garden_firefly").chunked(3)) {
                            Row {
                                for (id in group) {
                                    AnimalPortrait(Appearance(portrait = id), Modifier.size(size.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
        for (id in listOf("woodland_woodpecker", "woodland_chipmunk", "woodland_beaver", "woodland_skunk", "garden_firefly")) {
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

    @Test fun chipmunkStillOpensItsRealLockedCollectionDetail() {
        checkLockedDetail("Woodland", "woodland_chipmunk")
    }

    @Test fun beaverStillOpensItsRealLockedCollectionDetail() {
        checkLockedDetail("Woodland", "woodland_beaver")
    }

    @Test fun skunkStillOpensItsRealLockedCollectionDetail() {
        checkLockedDetail("Woodland", "woodland_skunk")
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

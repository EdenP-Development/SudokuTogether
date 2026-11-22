package com.playereden.sudokutogether

import org.junit.Assert.*
import org.junit.Test

class WoodlandPortraitScaleReviewTest {
    @Test fun sparseCanvasWoodlandPortraitsGetOnlyTheSmallVisualLift() {
        for (id in listOf("woodland_chipmunk", "woodland_beaver",
            "woodland_woodpecker", "woodland_skunk")) {
            assertEquals("$id uses source-resolution normalization", 1.22f,
                portraitDisplayScale(id), 0.001f)
            assertNotNull(animalDrawable(id))
        }
    }

    @Test fun legacyWoodlandAndOtherCollectionsKeepOriginalScaleAndDrawables() {
        for (id in listOf("woodland_rabbit", "woodland_bear", "woodland_fox",
                "woodland_owl", "woodland_raccoon", "woodland_badger",
                "ocean_seahorse", "ocean_dolphin", "garden_ladybug",
                "farm_cow")) {
            assertEquals("$id must keep original display size", 1f,
                portraitDisplayScale(id), 0.001f)
            assertNotNull(animalDrawable(id))
        }
    }

    @Test fun unknownPortraitKeepsNeutralSizeAndFallsBackSafely() {
        assertEquals(1f, portraitDisplayScale("woodland_not_packaged"), 0f)
        assertNull(animalDrawable("woodland_not_packaged"))
    }
}

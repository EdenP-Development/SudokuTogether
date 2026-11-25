package com.playereden.sudokutogether

import com.playereden.sudokutogether.core.Cosmetics
import org.junit.Assert.assertEquals
import org.junit.Test

class CompanionHabitatTest {
    @Test fun everyExistingPortraitCollectionMapsToItsOwnHabitat() {
        val groups = listOf(
            Cosmetics.woodlandPortraits to CompanionHabitat.WOODLAND,
            Cosmetics.oceanPortraits to CompanionHabitat.OCEAN,
            Cosmetics.gardenPortraits to CompanionHabitat.GARDEN,
            Cosmetics.farmPortraits to CompanionHabitat.FARM
        )
        for ((portraits, habitat) in groups) {
            for (portrait in portraits) {
                assertEquals(portrait.id, habitat, companionHabitat(portrait.id, animalDrawable(portrait.id) != null))
            }
        }
    }

    @Test fun missingArtworkAndUnknownCollectionsFallBackSafely() {
        assertEquals(CompanionHabitat.DEFAULT, companionHabitat("", false))
        assertEquals(CompanionHabitat.DEFAULT, companionHabitat("ocean_missing", false))
        assertEquals(CompanionHabitat.DEFAULT, companionHabitat("future_companion", true))
    }

    @Test fun farmCaptionDoesNotDescribeWoodland() {
        assertEquals("With your farm companion", companionHabitat("farm_cow", true).caption)
    }
}

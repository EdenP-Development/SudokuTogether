package com.playereden.sudokutogether

import org.junit.Assert.*
import org.junit.Test

/** Stable text decisions for the 3-column Collection layout. */
class Beta2VisualPolishCopyTest {
    @Test fun longSingleWordNamesUseOneLineCompactTreatment() {
        assertTrue(collectionNameIsLongSingleWord("Constellation"))
        assertFalse(collectionNameIsLongSingleWord("Focused Solver"))
        assertFalse(collectionNameIsLongSingleWord("Watering Can"))
        assertTrue(collectionNameIsLongSingleWord("Strawberry"))
        assertTrue(collectionNameIsLongSingleWord("Woodpecker"))
        assertTrue(collectionNameIsLongSingleWord("Dragonfly"))
        assertEquals(12, collectionIconLabelSize("Woodpecker"))
        assertEquals(11, collectionIconLabelSize("Constellation"))
        assertEquals(12, collectionIconLabelSize("Strawberry"))
        assertEquals(14, collectionIconLabelSize("Raccoon"))
        assertEquals(14, collectionIconLabelSize("Focused Solver"))
    }

    @Test fun numberSampleVisiblySeparatesTwoSudokuDigits() {
        assertEquals("5 9", NUMBER_PREVIEW_SAMPLE)
        assertFalse(NUMBER_PREVIEW_SAMPLE == "59")
    }
}

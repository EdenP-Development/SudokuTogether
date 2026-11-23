package com.playereden.sudokutogether

import org.junit.Assert.*
import org.junit.Test

/** Stable text decisions for the 3-column Collection layout. */
class Beta2VisualPolishCopyTest {
    @Test fun longSingleWordNamesUseOneLineCompactTreatment() {
        assertTrue(collectionNameIsLongSingleWord("Constellation"))
        assertFalse(collectionNameIsLongSingleWord("Focused Solver"))
        assertFalse(collectionNameIsLongSingleWord("Watering Can"))
        assertFalse(collectionNameIsLongSingleWord("Strawberry"))
    }

    @Test fun numberSampleVisiblySeparatesTwoSudokuDigits() {
        assertEquals("5 9", NUMBER_PREVIEW_SAMPLE)
        assertFalse(NUMBER_PREVIEW_SAMPLE == "59")
    }
}

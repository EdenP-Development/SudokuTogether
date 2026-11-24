package com.playereden.sudokutogether

import org.junit.Assert.*
import org.junit.Test

/** Six old reward IDs must remain identical; only the QA artwork changes. */
class InsigniaConceptMappingTest {
    @Test fun allSixNewRewardIdsHaveDifferentCustomVectorResources() {
        val ids = listOf("constellation", "sunrise", "focus", "compass", "moonlight", "grid_master")
        val resources = ids.map { candidateInsigniaDrawable(it) }
        assertTrue(resources.all { it != null })
        assertEquals(6, resources.toSet().size)
        assertNull(candidateInsigniaDrawable("tile"))
        assertNull(candidateInsigniaDrawable("leaf"))
        assertNull(candidateInsigniaDrawable("woodland_fox"))
    }

    @Test fun originalGlyphsAreStillAvailableForNormalAppBuilds() {
        val ids = listOf("constellation", "sunrise", "focus", "compass", "moonlight", "grid_master")
        ids.forEach { assertTrue(emblemSymbol(it).isNotBlank()) }
    }
}

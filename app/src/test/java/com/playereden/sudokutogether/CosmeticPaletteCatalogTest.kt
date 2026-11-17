package com.playereden.sudokutogether

import androidx.compose.ui.graphics.Color
import com.playereden.sudokutogether.core.Cosmetics
import org.junit.Assert.*
import org.junit.Test

/** A new registered cosmetic must not silently render using fallback colors. */
class CosmeticPaletteCatalogTest {
    @Test fun everyRegisteredBoardHasExplicitUniqueVisualMetadata() {
        val ids = Cosmetics.boards.map { it.id }
        assertEquals("Add board palette metadata whenever the catalog expands",
            ids.toSet(), boardPalettes.keys)
        assertEquals(ids.size, ids.map(::boardFillFor).toSet().size)
        assertEquals(setOf("midnight", "circuit"), ids.filter(::boardIsDark).toSet())
        assertEquals(Paper, boardFillFor("classic"))
        assertEquals(Color(0xFFFFF6DF), boardFillFor("linen"))
        assertEquals(Color(0xFF152A36), boardFillFor("midnight"))
        assertEquals(Color(0xFF102A2B), boardFillFor("circuit"))
        assertEquals(Paper, boardFillFor("unrecognized-board"))
        assertFalse(boardIsDark("unrecognized-board"))
    }

    @Test fun everyRegisteredInkHasDistinctLightAndDarkRenderColors() {
        val ids = Cosmetics.inks.map { it.id }
        assertEquals("Add light and dark ink colors whenever the catalog expands",
            ids.toSet(), inkPalettes.keys)
        assertEquals(ids.size, ids.map { inkFor(it, false) }.toSet().size)
        assertEquals(ids.size, ids.map { inkFor(it, true) }.toSet().size)
        assertEquals(Forest, inkFor("forest", false))
        assertEquals(Color(0xFF8DE8BB), inkFor("forest", true))
        assertEquals(inkFor("forest", false), inkFor("unrecognized-ink", false))
        assertEquals(inkFor("forest", true), inkFor("unrecognized-ink", true))
    }
}

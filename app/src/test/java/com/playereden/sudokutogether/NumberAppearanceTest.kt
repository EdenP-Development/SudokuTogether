package com.playereden.sudokutogether

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontListFontFamily
import com.playereden.sudokutogether.core.Appearance
import com.playereden.sudokutogether.core.Cosmetics
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NumberAppearanceTest {
    @Test fun savedFontIdsResolveToDistinctPackagedFamilies() {
        val families = Cosmetics.fonts.associate { it.id to fontFor(it.id) }

        for ((id, family) in families) {
            assertTrue("$id should resolve to a bundled font family", family is FontListFontFamily)
        }
        assertEquals(Cosmetics.fonts.size, families.values.toSet().size)
    }

    @Test fun unrecognizedFontUsesClear() {
        assertEquals(fontFor(Appearance().font), fontFor("unrecognized_font"))
    }

    @Test fun classicDisplayOnlyOverridesTheShownFontAndInk() {
        val equipped = Appearance(
            ink = "rose", board = "linen", font = "serif", emblem = "leaf", portrait = "woodland_fox"
        )
        assertEquals(equipped.copy(ink = "forest", font = "clear"), equipped.withNumberDisplay(true))
        assertEquals(equipped, equipped.withNumberDisplay(false))
    }

    @Test fun newInksStayReadableOnBoardAndSelectionColors() {
        val light = listOf(boardFillFor("classic"), boardFillFor("linen"), AmberLight, TealLight)
        val dark = listOf(boardFillFor("midnight"), boardFillFor("circuit"), Color(0xFF555336), Color(0xFF305C55))
        for (id in listOf("lagoon", "clay", "slate")) {
            val lightInk = inkFor(id, false).luminance()
            val darkInk = inkFor(id, true).luminance()
            for (background in light) {
                assertTrue("$id on $background", (background.luminance() + .05f) / (lightInk + .05f) >= 4.5f)
            }
            for (background in dark) {
                assertTrue("$id on $background", (darkInk + .05f) / (background.luminance() + .05f) >= 4.5f)
            }
        }
    }
}

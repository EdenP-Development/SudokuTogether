package com.playereden.sudokutogether.core

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GardenCosmeticsTest {
    @Test fun aFreshProfileCanUseTheHoneybeeAndLagoonInk() {
        val style = Appearance(ink = "lagoon", portrait = "garden_honeybee")
        assertTrue(Cosmetics.valid(style))
        assertEquals(style, Cosmetics.accessible(style, emptyList()))
    }

    @Test fun existingCoopFinishesCountOnceTowardTheNewStyles() {
        val finishes = List(20) { index -> GameRecord("coop-$index", COOP, "SOLVED", "puzzle", 1000, 1, 1000) }
        val excluded = listOf(
            finishes.first(),
            GameRecord("race-loss", VERSUS, "LOSS", "puzzle", 1000, 1, 1000)
        )
        val styles = listOf(
            2 to Appearance(font = "rounded"),
            4 to Appearance(ink = "clay"),
            4 to Appearance(portrait = "garden_ladybug"),
            8 to Appearance(font = "pencil"),
            10 to Appearance(ink = "slate"),
            10 to Appearance(portrait = "garden_butterfly"),
            20 to Appearance(portrait = "garden_dragonfly")
        )
        for ((target, style) in styles) {
            assertTrue("Missing cosmetic: $style", Cosmetics.valid(style))
            assertEquals(Appearance(), Cosmetics.accessible(style, finishes.take(target - 1) + excluded))
            assertEquals(style, Cosmetics.accessible(style, finishes.take(target) + excluded))
        }
    }

    @Test fun newStylesRoundTripInNearbyMessages() {
        val style = Appearance(ink = "slate", font = "pencil", portrait = "garden_dragonfly")
        val encoded = ByteArrayOutputStream()
        Protocol.write(encoded, Message("hello", appearance = style))
        val decoded = Protocol.read(ByteArrayInputStream(encoded.toByteArray()))
        assertEquals(8, decoded.version)
        assertEquals(style, decoded.appearance)
        assertFalse(Cosmetics.valid(style.copy(portrait = "garden_unrecognized")))
    }
}

package com.playereden.sudokutogether

import com.playereden.sudokutogether.core.Cosmetics
import org.junit.Assert.*
import org.junit.Test

/** Keeps late Woodland PNG presentation in scale without modifying source artwork. */
class WoodlandPortraitRenderBalanceTest {
    @Test fun onlyFourSourcePaddedWoodlandIconsGetModerateScaleCompensation() {
        val adjusted = mapOf(
            "woodland_chipmunk" to 1.30f,
            "woodland_beaver" to 1.28f,
            "woodland_woodpecker" to 1.26f,
            "woodland_skunk" to 1.28f
        )
        val ids = Cosmetics.portraits.map { it.id }
        assertTrue(ids.containsAll(adjusted.keys))
        for (id in ids) {
            assertNotNull("$id must remain drawable", animalDrawable(id))
            val expected = adjusted[id] ?: 1f
            assertEquals("$id render factor", expected, portraitDisplayScale(id), .0001f)
            assertTrue("Avoid excessive circle crop: $id",
                portraitDisplayScale(id) >= 1f && portraitDisplayScale(id) <= 1.31f)
        }
        assertEquals(1f, portraitDisplayScale("unknown-icon"), .0001f)
        assertEquals(1f, portraitDisplayScale(""), .0001f)
    }
}

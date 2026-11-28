package com.playereden.sudokutogether

import org.junit.Assert.assertEquals
import org.junit.Test

class StartupBrandingTest {
    @Test fun homeHeaderUsesApprovedMasterDerivedResource() {
        assertEquals(R.drawable.gridmates_header_approved, approvedGridMatesHeaderDrawable())
        org.junit.Assert.assertNotEquals(R.drawable.ic_launcher_foreground, approvedGridMatesHeaderDrawable())
    }

    @Test fun sharedTimingTrial() {
        assertEquals(300, STARTUP_FADE_MS)
        assertEquals(1500, STARTUP_HOLD_MS)
        assertEquals(4200, 2 * (2 * STARTUP_FADE_MS + STARTUP_HOLD_MS))
    }

    @Test fun missingSeriesArtworkDoesNotCreatePlaceholderStage() {
        assertEquals(listOf(R.drawable.spudacus_v1_full_transparent), startupBrandingStages())
    }

    @Test fun approvedSeriesStageFollowsDeveloperStage() {
        assertEquals(listOf(R.drawable.spudacus_v1_full_transparent, R.drawable.gridmates_v1_runtime_transparent), startupBrandingStages(R.drawable.gridmates_v1_runtime_transparent))
    }
}

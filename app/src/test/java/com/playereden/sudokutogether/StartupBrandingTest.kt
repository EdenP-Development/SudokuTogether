package com.playereden.sudokutogether

import org.junit.Assert.assertEquals
import org.junit.Test

class StartupBrandingTest {
    @Test fun missingSeriesArtworkDoesNotCreatePlaceholderStage() {
        assertEquals(listOf(R.drawable.spudacus_v1_full_transparent), startupBrandingStages())
    }

    @Test fun approvedSeriesStageFollowsDeveloperStage() {
        assertEquals(listOf(R.drawable.spudacus_v1_full_transparent, R.drawable.gridmates_v1_runtime_transparent), startupBrandingStages(R.drawable.gridmates_v1_runtime_transparent))
    }
}

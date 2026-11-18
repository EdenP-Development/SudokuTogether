package com.playereden.sudokutogether

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InstallerSharingEligibilityTest {
    @Test fun productionStandaloneInstallerCanBeShared() {
        assertTrue(canShareInstalledApk(debugBuild = false, hasSplits = false))
    }

    @Test fun qaAndDeveloperApksAreNeverSharedAsPublicInstallers() {
        assertFalse(canShareInstalledApk(debugBuild = true, hasSplits = false))
        assertFalse(canShareInstalledApk(debugBuild = true, hasSplits = true))
    }

    @Test fun appBundleSplitsCannotBeSharedAsAStandaloneApk() {
        assertFalse(canShareInstalledApk(debugBuild = false, hasSplits = true))
    }
}

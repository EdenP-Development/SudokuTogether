package com.playereden.sudokutogether

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.playereden.sudokutogether.core.GameRecord
import com.playereden.sudokutogether.core.SOLO
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/** Public-facing checks without modifying a real player's stored data. */
class PublicReleaseCopyUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun aboutUsesCurrentVersionAndExplainsSafeProgressAndTrustedWifi() {
        compose.setContent {
            SudokuTogetherTheme {
                AboutGame(onDismiss = {}, share = null)
            }
        }
        compose.onNodeWithTag("about_gridmates").assertExists()
        compose.onNodeWithText("Version ${BuildConfig.VERSION_NAME}").assertExists()
        compose.onNodeWithText("Your progress").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Nearby privacy").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Share game installer").assertDoesNotExist()
        compose.onNodeWithText("Done").performClick()
        // Dismissal is the host callback; the dialog is still composed by
        // this test's fixed input, so no fake navigation is asserted.
    }

    @Test fun permittedSharingExplainsThatSavedProgressIsNeverInAnApk() {
        compose.setContent { SudokuTogetherTheme { AboutGame({}, share = {}) } }
        compose.onNodeWithText("Share game installer").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Shares the installed game APK, not your saved puzzles or progress.",
            substring = true).performScrollTo().assertIsDisplayed()
    }

    @Test fun restorePreviewShowsRealEarnedItemCountForLegacyBackups() {
        val matches = (1..5).map {
            GameRecord("finish-$it", SOLO, "SOLVED", "puzzle-$it", 90_000L, 35, it.toLong(), "EASY")
        }
        // Original Beta 1 backups contain saved records, not an entitlement ledger.
        val preview = BackupPreview("1.0.0-beta.1", 1000L,
            BackupProgress(null, emptyList(), matches), BackupSettings())
        assertTrue(preview.earnedCosmeticCount > 0)
        compose.setContent {
            SudokuTogetherTheme {
                ProgressBackupRestoreDialog(preview, enabled = true, dismiss = {}, restore = {})
            }
        }
        compose.onNodeWithText("Earned Collection items · ${preview.earnedCosmeticCount}")
            .performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Back up your current progress first if you might want to keep it.")
            .performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Cancel").assertExists()
    }

    @Test fun restorePreviewAlsoCountsEntitlementsAfterOldRecordsWereTrimmed() {
        val preview = BackupPreview("1.0.0-beta.2", 1000L,
            BackupProgress(null, emptyList(), emptyList(),
                ownedCosmeticIds = listOf("rose", "woodland_robin")), BackupSettings())
        assertTrue(preview.earnedCosmeticCount == 2)
        compose.setContent {
            SudokuTogetherTheme {
                ProgressBackupRestoreDialog(preview, enabled = true, dismiss = {}, restore = {})
            }
        }
        compose.onNodeWithText("Earned Collection items · 2").performScrollTo().assertIsDisplayed()
    }

    @Test fun progressBackupActionsStayAvailableAtLargeFonts() {
        compose.setContent {
            SudokuTogetherTheme {
                CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, 2f)) {
                    Box(Modifier.width(320.dp).fillMaxHeight()) {
                        ProgressBackupSettings(ProgressBackupUiState(), enabled = true,
                            export = {}, open = {})
                    }
                }
            }
        }
        compose.onNodeWithText("Back up progress").assertIsEnabled()
        compose.onNodeWithText("Restore backup").assertIsEnabled()
        compose.onNodeWithText("Active nearby multiplayer rooms are not included in this backup.")
            .assertExists()
    }
}

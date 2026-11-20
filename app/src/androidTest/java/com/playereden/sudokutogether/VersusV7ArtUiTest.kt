package com.playereden.sudokutogether

import android.graphics.Bitmap
import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import java.io.File

/** Real Android screenshot and UI assertions for new canonical-mascot Versus V7 artwork. */
class VersusV7ArtUiTest {
    @get:Rule val compose = createEmptyComposeRule()

    @Test fun versusV7PreservesNativeActionsAndHasNoDuplicateMascots() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            lateinit var activity: MainActivity
            scenario.onActivity { a ->
                activity = a
                a.setContent {
                    SudokuTogetherTheme { SudokuApp(GameUiState(), polishActions(), {}) }
                }
            }
            EmulatorSystemDialogs.dismiss()
            compose.onNodeWithTag("home_mode_versus").performScrollTo().performClick()
            compose.onNodeWithTag("nearby_art_versus", useUnmergedTree = true).assertIsDisplayed()
            compose.onNodeWithTag("nearby_art_image", useUnmergedTree = true).assertExists()
            compose.onNodeWithTag("nearby_art_v7", useUnmergedTree = true).assertExists()
            compose.onNodeWithTag("nearby_art_v5", useUnmergedTree = true).assertDoesNotExist()
            compose.onNodeWithTag("nearby_art_motif", useUnmergedTree = true).assertDoesNotExist()
            compose.onNodeWithTag("nearby_art_fox", useUnmergedTree = true).assertDoesNotExist()
            compose.onNodeWithTag("nearby_art_owl", useUnmergedTree = true).assertDoesNotExist()
            compose.onNodeWithText("FRIENDLY COMPETITION").assertExists()
            compose.onNodeWithText("Race to finish").assertIsDisplayed()
            compose.onNodeWithText("Start a race").performScrollTo().assertIsDisplayed()
            compose.onNodeWithText("Join a game").performScrollTo().assertIsDisplayed()
            compose.onNodeWithTag("require_code").performScrollTo().assertExists()

            // Return to banner before capture: scroll assertions may have moved it.
            compose.onNodeWithTag("nearby_art_versus").performScrollTo().assertIsDisplayed()
            NativeWindowCapture.save(activity,
                File(context.filesDir, "gridmates-versus-v7-android.png"))

            // Co-op remains on the previously approved V5 art; no V7 leakage.
            compose.onNodeWithTag("home_mode_together").performScrollTo().performClick()
            compose.onNodeWithTag("nearby_art_coop", useUnmergedTree = true).assertIsDisplayed()
            compose.onNodeWithTag("nearby_art_v5", useUnmergedTree = true).assertExists()
            compose.onNodeWithTag("nearby_art_v7", useUnmergedTree = true).assertDoesNotExist()
            compose.onNodeWithText("Shared board").assertIsDisplayed()
            NativeWindowCapture.save(activity,
                File(context.filesDir, "gridmates-coop-v5-control.png"))
        }
    }
}

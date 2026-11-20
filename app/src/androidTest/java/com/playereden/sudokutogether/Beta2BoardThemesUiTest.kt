package com.playereden.sudokutogether

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.test.platform.app.InstrumentationRegistry
import android.graphics.Bitmap
import java.io.File
import org.junit.Rule
import org.junit.Test

/** Dedicated isolated reviewer for four new board unlock cards. */
class Beta2BoardThemesUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun newBoardsShowTheirRealLockedProgressAndPreview() {
        compose.setContent {
            SudokuTogetherTheme {
                SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {})
            }
        }
        compose.onNodeWithTag("nav_collection").performClick()
        compose.onNodeWithText("Boards").performClick()
        compose.onNodeWithText("1 of 11 available").assertIsDisplayed()
        val required = listOf("moss" to 16, "rosewood" to 28,
            "pebble" to 48, "tidal" to 70)
        for ((id, count) in required) {
            compose.onNodeWithTag("cosmetic_board_$id").performScrollTo().performClick()
            compose.onNodeWithText("Complete $count puzzles").assertIsDisplayed()
            // Capture the REAL Compose-rendered board preview, not a mockup.
            val preview = compose.onNodeWithTag("expanded_board_preview").performScrollTo()
            val bitmap = preview.captureToImage().asAndroidBitmap()
            val file = File(InstrumentationRegistry.getInstrumentation().targetContext.filesDir,
                "board-$id.png")
            file.outputStream().use { output ->
                check(bitmap.compress(Bitmap.CompressFormat.PNG, 100, output))
            }
            compose.onNodeWithText("Close").performClick()
        }
    }
}

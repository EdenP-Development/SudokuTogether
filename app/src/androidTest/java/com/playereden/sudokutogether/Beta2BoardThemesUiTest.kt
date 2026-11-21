package com.playereden.sudokutogether

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.test.platform.app.InstrumentationRegistry
import android.graphics.Bitmap
import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
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
            // Publish to shared media: Gradle instrumentation can uninstall the
            // app after tests, so private filesDir snapshots do not survive.
            val outputBytes = ByteArrayOutputStream()
            check(bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputBytes))
            check(outputBytes.size() > 500) { "Board $id screenshot unexpectedly tiny" }
            val resolver = InstrumentationRegistry.getInstrumentation().targetContext.contentResolver
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "gridmates-board-$id.png")
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH,
                    "${Environment.DIRECTORY_PICTURES}/GridMatesBeta2BoardQA")
            }
            val uri = checkNotNull(resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)) {
                "Could not create Android media screenshot for $id"
            }
            resolver.openOutputStream(uri)?.use { output ->
                output.write(outputBytes.toByteArray())
            } ?: error("Cannot write Android media screenshot for $id")
            compose.onNodeWithText("Close").performClick()
        }
    }
}

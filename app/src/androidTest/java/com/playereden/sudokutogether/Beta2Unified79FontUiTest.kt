package com.playereden.sudokutogether

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import java.io.ByteArrayOutputStream

/** Capture genuine Android Collection preview for each newly packaged font. */
class Beta2Unified79FontUiTest {
    @get:Rule val compose = createComposeRule()

    @Test fun lastFourFontsShowReadableOneThroughNineAndRealLockedRequirements() {
        compose.setContent {
            SudokuTogetherTheme { SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {}) }
        }
        compose.onNodeWithTag("nav_collection").performClick()
        compose.onNodeWithText("Numbers").performClick()
        compose.onNodeWithText("Number fonts").performClick()
        val additions = linkedMapOf("typewriter" to 14, "editorial" to 26,
            "soft_print" to 45, "tall_print" to 75)
        for ((id, finishTarget) in additions) {
            compose.onNodeWithTag("cosmetic_font_$id").performScrollTo().performClick()
            compose.onNodeWithText("Complete $finishTarget puzzles").assertIsDisplayed()
            for (digit in '1'..'9') {
                compose.onNodeWithText(digit.toString(), useUnmergedTree = true).assertExists()
            }
            compose.waitForIdle()
            // Capture the actual rendered 1-9 font grid, not the underlying
            // activity root (which produced four identical PNGs in prior CI).
            val board = compose.onNodeWithTag("expanded_board_preview").performScrollTo()
            board.assertExists()
            val image = board.captureToImage().asAndroidBitmap()
            val output = ByteArrayOutputStream()
            check(image.compress(Bitmap.CompressFormat.PNG, 100, output))
            check(output.size() > 1000) { "$id failed screenshot capture" }
            val resolver = InstrumentationRegistry.getInstrumentation().targetContext.contentResolver
            val fields = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "gridmates-font-$id.png")
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH,
                    "${Environment.DIRECTORY_PICTURES}/GridMatesBeta2Unified79FontQA")
            }
            val uri = checkNotNull(resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, fields)) {
                "$id MediaStore screenshot URI unavailable"
            }
            resolver.openOutputStream(uri)?.use { stream ->
                stream.write(output.toByteArray())
            } ?: error("Cannot save Android font screenshot for $id")
            compose.onNodeWithText("Close").performClick()
        }
    }
}

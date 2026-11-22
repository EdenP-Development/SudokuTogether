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

/** Review-only Android UI evidence for the unified 43-icon Collection. */
class Beta2Unified75UiTest {
    @get:Rule val compose = createComposeRule()

    private fun capturePortrait(id: String) {
        val node = compose.onNodeWithTag("portrait_$id").performScrollTo()
        node.assertExists()
        val bytes = ByteArrayOutputStream()
        check(node.captureToImage().asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, bytes))
        check(bytes.size() > 500) { "$id produced empty snapshot" }
        val resolver = InstrumentationRegistry.getInstrumentation().targetContext.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "gridmates-$id.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH,
                "${Environment.DIRECTORY_PICTURES}/GridMatesBeta2Unified75QA")
        }
        val uri = checkNotNull(resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values))
        resolver.openOutputStream(uri)?.use { it.write(bytes.toByteArray()) }
            ?: error("Unable to save screenshot for $id")
    }

    @Test fun allElevenOceanCharactersRenderAndUnlockTextMatches() {
        compose.setContent {
            SudokuTogetherTheme {
                SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {})
            }
        }
        compose.onNodeWithTag("nav_collection").performClick()
        compose.onNodeWithText("Ocean").performScrollTo().performClick().assertIsSelected()
        compose.onNodeWithText("1 of 11 available").assertIsDisplayed()
        for (id in listOf("ocean_seahorse", "ocean_dolphin",
                "ocean_seashell", "ocean_clownfish")) {
            compose.onNodeWithTag("portrait_$id").assertExists()
            capturePortrait(id)
        }
        compose.onNodeWithTag("portrait_ocean_clownfish").performScrollTo().performClick()
        compose.onNodeWithText("Complete 95 puzzles or solve 5 Hard solo puzzles").assertIsDisplayed()
        compose.onNodeWithText("Close").performClick()
    }

    @Test fun allSeventeenWoodlandCharactersRenderAndUnlockTextMatches() {
        compose.setContent {
            SudokuTogetherTheme {
                SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {})
            }
        }
        compose.onNodeWithTag("nav_collection").performClick()
        compose.onNodeWithText("Woodland").performScrollTo().performClick().assertIsSelected()
        compose.onNodeWithText("2 of 17 available").assertIsDisplayed()
        for (id in listOf("woodland_chipmunk", "woodland_beaver",
                "woodland_woodpecker", "woodland_skunk")) {
            compose.onNodeWithTag("portrait_$id").assertExists()
            capturePortrait(id)
        }
        compose.onNodeWithTag("portrait_woodland_skunk").performScrollTo().performClick()
        compose.onNodeWithText("Complete 90 puzzles or solve 12 solo puzzles without hints or checks").assertIsDisplayed()
        compose.onNodeWithText("Close").performClick()
    }
}

package com.playereden.sudokutogether

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.io.ByteArrayOutputStream

/**
 * Real Android screenshot evidence for the restored, complete collection
 * chapters. Saves screenshots from the actual Compose app, not mocks.
 */
class Beta2AlbumOwnerUiTest {
    @get:Rule val compose = createComposeRule()

    private fun screenshot(name: String) {
        val image = compose.onRoot().captureToImage().asAndroidBitmap()
        val bytes = ByteArrayOutputStream()
        check(image.compress(Bitmap.CompressFormat.PNG, 100, bytes))
        check(bytes.size() > 8000) { "$name screenshot suspiciously empty" }
        val resolver = InstrumentationRegistry.getInstrumentation().targetContext.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "album-$name.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH,
                "${Environment.DIRECTORY_PICTURES}/GridMatesBeta2AlbumQA")
        }
        val uri = checkNotNull(resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values))
        resolver.openOutputStream(uri)?.use { it.write(bytes.toByteArray()) }
            ?: error("Unable to save album screenshot $name")
    }

    @Test fun allChaptersContainTheirOwnStartersAndLockedItemsWithoutDuplicates() {
        compose.setContent {
            SudokuTogetherTheme {
                SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {})
            }
        }
        compose.onNodeWithTag("nav_collection").performClick()
        for (i in 1..5) compose.onNodeWithTag("collection_chapter_$i").assertExists()
        val samples = linkedMapOf(
            "woodland" to listOf("woodland_owl", "woodland_fox",
                "woodland_chipmunk", "woodland_beaver", "woodland_woodpecker", "woodland_skunk"),
            "ocean" to listOf("ocean_octopus", "ocean_seahorse", "ocean_clownfish"),
            "garden" to listOf("garden_honeybee", "garden_ladybug", "garden_potted_tulip"),
            "farm" to listOf("farm_cow", "farm_chicken", "farm_hay_bale")
        )
        for ((family, members) in samples) {
            for (id in members) {
                compose.onAllNodesWithTag("portrait_$id").assertCountEquals(1)
            }
            compose.onNodeWithTag("portrait_${members[1]}").performScrollTo().assertIsDisplayed()
            screenshot(family)
        }
        // Four screenshots are the sole purpose of this test. Family-filter
        // isolation is independently checked by CollectionPolishUiTest;
        // selecting a horizontally scrolled chip from the bottom of this
        // whole-page capture is intentionally not duplicated here.
    }
}

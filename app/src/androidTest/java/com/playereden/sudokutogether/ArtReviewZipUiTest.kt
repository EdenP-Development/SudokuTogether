package com.playereden.sudokutogether

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.playereden.sudokutogether.core.Cosmetics
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ArtReviewZipUiTest {
    @get:Rule val compose = createAndroidComposeRule<ArtReviewActivity>()

    @Test fun reviewerLauncherShowsOriginalsAndImportButDoesNotAddRewards() {
        compose.onNodeWithText("GridMates · Art Review").assertIsDisplayed()
        compose.onNodeWithTag("art_review_load_zip").assertIsDisplayed()
        compose.onNodeWithTag("art_review_farm_goat").assertExists()
        assertEquals(7, artReviewSamples.size)
        assertEquals(7, artReviewSamples.map { it.id }.distinct().size)
        assertEquals(79, Cosmetics.knownIds.size)
        assertFalse("QA candidates cannot enter the real catalog", "farm_goat" in Cosmetics.knownIds)
        assertFalse("QA candidates cannot enter the real catalog", "garden_daisy" in Cosmetics.knownIds)
    }

    @Test fun zipAcceptsSevenCandidatePngsWithNestedFolderNames() {
        val images = ArtReviewZipReader.read(ByteArrayInputStream(zipWithIds(artReviewSamples.map { it.id })))
        assertEquals(7, images.size)
        assertEquals(512, images.getValue("farm_barn").width)
    }

    @Test fun missingCandidateFailsClosed() {
        try {
            ArtReviewZipReader.read(ByteArrayInputStream(zipWithIds(artReviewSamples.dropLast(1).map { it.id })))
            throw AssertionError("Expected import to fail with a missing Sea Star")
        } catch (expected: IllegalArgumentException) {
            assert(expected.message?.contains("ocean_sea_star") == true)
        }
    }

    private fun zipWithIds(ids: List<String>): ByteArray {
        val bytes = ByteArrayOutputStream()
        ZipOutputStream(bytes).use { zip ->
            for (id in ids) {
                zip.putNextEntry(ZipEntry("GridMates_Beta2_Style_Match_Review/${id}_candidate.png"))
                val bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888)
                bitmap.eraseColor(Color.TRANSPARENT)
                val canvas = android.graphics.Canvas(bitmap)
                val paint = android.graphics.Paint().apply { color = Color.RED }
                canvas.drawCircle(256f, 256f, 160f, paint)
                val png = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, png)
                zip.write(png.toByteArray())
                zip.closeEntry()
                bitmap.recycle()
            }
        }
        return bytes.toByteArray()
    }
}

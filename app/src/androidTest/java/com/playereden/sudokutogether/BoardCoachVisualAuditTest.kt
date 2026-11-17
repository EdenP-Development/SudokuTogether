package com.playereden.sudokutogether

import android.graphics.Bitmap
import android.content.ContentValues
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.playereden.sudokutogether.core.*
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.ByteArrayOutputStream

/**
 * Synthetic, repeatable visual audit only; never reads personal game data.
 * Screenshots are for comparing board readability and Coach dialog layout,
 * not substitutes for the owner's actual phone review.
 */
class BoardCoachVisualAuditTest {
    @get:Rule val compose = createComposeRule()
    private val target = InstrumentationRegistry.getInstrumentation().targetContext

    private fun screenshot(name: String) {
        compose.waitForIdle()
        val automation = InstrumentationRegistry.getInstrumentation().uiAutomation
        automation.waitForIdle(350, 5000)
        // UIAutomation can return the PREVIOUS compositor frame immediately
        // after Compose reports idle. Read a warm-up frame and verify the
        // intended puzzle palette before accepting the exported image.
        var image: Bitmap? = null
        var lastDarkFraction = 0f
        for (attempt in 0..6) {
            val frame = requireNotNull(automation.takeScreenshot())
            var dark = 0
            var sampled = 0
            for (y in 100 until frame.height - 100 step 16) {
                for (x in 20 until frame.width - 20 step 16) {
                    val pixel = frame.getPixel(x, y)
                    if (android.graphics.Color.red(pixel) < 115 &&
                        android.graphics.Color.green(pixel) < 115 &&
                        android.graphics.Color.blue(pixel) < 115) dark++
                    sampled++
                }
            }
            lastDarkFraction = dark.toFloat() / sampled
            val sceneReady = when {
                name.startsWith("01-") -> lastDarkFraction in .01f..10f / 100f
                name.startsWith("02-") -> lastDarkFraction > .15f
                else -> lastDarkFraction > .20f
            }
            if (attempt > 0 && sceneReady) {
                image = frame
                break
            }
            frame.recycle()
            compose.waitForIdle()
            Thread.sleep(180)
        }
        val captured = checkNotNull(image) {
            "Screenshot did not reach expected scene: $name (dark fraction $lastDarkFraction)"
        }
        val bytes = ByteArrayOutputStream().use { stream ->
            assertTrue("Unable to encode $name", captured.compress(Bitmap.CompressFormat.PNG, 100, stream))
            stream.toByteArray()
        }
        captured.recycle()
        assertTrue("Screenshot missing or empty", bytes.size > 10000)

        // Save through Android's scoped-storage API. Instrumentation may clear
        // app-private external files after testing, and shell cannot reliably
        // copy Android/data on API 36.
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$name.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/GridMates-Beta12-Visual")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        val resolver = target.contentResolver
        val uri = requireNotNull(resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)) {
            "Unable to create MediaStore screenshot $name"
        }
        requireNotNull(resolver.openOutputStream(uri)).use { it.write(bytes) }
        resolver.update(uri, ContentValues().apply {
            put(MediaStore.Images.Media.IS_PENDING, 0)
        }, null, null)
    }

    @Test fun capturesActualPuzzleBoardWithNotesOnClassicAndMidnight() {
        val clues = "530070000600195000098000060800060003400803001700020006060000280000419005000080079"
            .map(Char::digitToInt)
        val puzzle = Puzzle("visual-notes", clues, "EASY")
        val cells = clues.mapIndexed { i, value ->
            Cell(value, if (value == 0 && i % 3 == 2) 0b001010101 else 0)
        }
        val snapshot = Snapshot("visual-notes", "visual", puzzle, cells, mode = SOLO)
        val state = mutableStateOf(GameUiState(page = Page.PLAY, role = SOLO, selected = 2,
            notes = true, snapshot = snapshot))

        compose.setContent { SudokuTogetherTheme { SudokuApp(state.value, polishActions(), {}) } }
        compose.onNodeWithTag("board_frame").assertIsDisplayed()
        compose.onNodeWithTag("number_1").assertIsDisplayed()
        screenshot("01-classic-pencil-notes")

        compose.runOnIdle {
            state.value = state.value.copy(appearance = Appearance(board = "midnight", ink = "rose", font = "pencil"))
        }
        compose.onNodeWithTag("board_frame").assertIsDisplayed()
        screenshot("02-midnight-rose-pencil-notes")
    }

    @Test fun capturesCoachExplanationAndCrossedOffNotes() {
        val values = "600945002002673000054128060001567400576894231849312006003056920000031600465289003"
            .map(Char::digitToInt)
        val puzzle = Puzzle("visual-coach", values, "MEDIUM")
        val hint = requireNotNull(SudokuAssistance.nextCoachHint(puzzle, values))
        val snapshot = Snapshot("visual-coach", "visual", puzzle, values.map { Cell(it) }, mode = SOLO)
        val state = GameUiState(page = Page.PLAY, role = SOLO, snapshot = snapshot, hint = hint)
        compose.setContent {
            SudokuTogetherTheme { PuzzleCoachDialog(state, polishActions()) }
        }
        compose.onNodeWithText("Show why").assertIsDisplayed()
        screenshot("03-coach-introduction")
        compose.onNodeWithText("Show why").performScrollTo().performClick()
        compose.onNodeWithText("Step 1 of 2").assertIsDisplayed()
        compose.onNodeWithText("Cross off 7 at row 7, column 9.").assertExists()
        compose.onNodeWithTag("coach_cell_78").assertExists()
        screenshot("04-coach-deduction")
    }
}

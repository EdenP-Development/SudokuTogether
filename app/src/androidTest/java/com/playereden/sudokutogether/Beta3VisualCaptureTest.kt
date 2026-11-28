package com.playereden.sudokutogether

import android.content.ContentValues
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.playereden.sudokutogether.core.*
import org.junit.Rule
import org.junit.Test

/** Synthetic screenshots only. No owner data, no changes to production behavior. */
class Beta3VisualCaptureTest {
    @get:Rule val compose = createComposeRule()

    private fun capture(name: String) {
        compose.waitForIdle()
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val automation = instrumentation.uiAutomation
        automation.waitForIdle(350, 5000)
        // Allow the compositor to present the frame after semantic idle.
        automation.takeScreenshot()?.recycle()
        Thread.sleep(250)
        val bitmap = requireNotNull(automation.takeScreenshot())
        val resolver = instrumentation.targetContext.contentResolver
        val uri = requireNotNull(resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$name.png")
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/GridMates-Beta3-Visual")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }))
        try {
            requireNotNull(resolver.openOutputStream(uri)).use {
                check(bitmap.compress(Bitmap.CompressFormat.PNG, 100, it))
            }
            resolver.update(uri, ContentValues().apply {
                put(MediaStore.Images.Media.IS_PENDING, 0)
            }, null, null)
        } catch (error: Throwable) {
            resolver.delete(uri, null, null)
            throw error
        } finally { bitmap.recycle() }
    }

    @Test fun home() {
        compose.setContent { SudokuTogetherTheme { SudokuApp(GameUiState(), polishActions(), {}) } }
        compose.onNodeWithTag("gridmates_approved_header_mark").assertIsDisplayed()
        capture("01-home")
    }

    @Test fun profileAndNames() {
        compose.setContent { SudokuTogetherTheme {
            SudokuApp(GameUiState(page = Page.PROFILE, name = "Happy Maple Fox"), polishActions(), {})
        } }
        compose.onNodeWithTag("profile_player_card").assertIsDisplayed()
        capture("02-profile")
        compose.onNodeWithText("Name Collection").performScrollTo().performClick()
        compose.onNodeWithTag("name_album_slot_first").performScrollTo().assertIsDisplayed()
        capture("03-name-collection")
        compose.onNodeWithText("Build your name").performScrollTo().performClick()
        compose.onNodeWithTag("name_preview").assertExists()
        capture("04-name-builder")
        compose.onNodeWithTag("name_first_picker").performScrollTo().performClick()
        compose.onNodeWithTag("name_option_first.happy").assertIsSelected()
        capture("05-name-dropdown")
    }

    @Test fun collectionAndSettings() {
        compose.setContent { SudokuTogetherTheme {
            SudokuApp(GameUiState(page = Page.PROFILE), polishActions(), {})
        } }
        compose.onNodeWithTag("nav_collection").performClick()
        compose.onNodeWithTag("collection_ownership_AVAILABLE").performScrollTo().performClick()
        compose.onNodeWithTag("collection_available_gallery").performScrollTo().assertIsDisplayed()
        capture("06-available-icons")
        compose.onNodeWithText("Numbers").performScrollTo().performClick()
        compose.onNodeWithText("Number fonts").performScrollTo().performClick()
        compose.onNodeWithTag("cosmetic_font_clear").performScrollTo().assertIsDisplayed()
        capture("07-number-fonts")
        compose.onNodeWithTag("nav_settings").performClick()
        compose.onNodeWithText("Clean pencil marks").performScrollTo().assertIsDisplayed()
        capture("08-settings")
    }

    @Test fun boardAndFocusedCoach() {
        val clues = List(81) { 0 }.toMutableList().apply { this[15] = 9; this[28] = 4 }
        val values = clues.toMutableList().apply { this[16] = 9; this[31] = 4; this[37] = 4 }
        val puzzle = Puzzle("visual-conflicts", clues, "EASY")
        val snapshot = Snapshot("visual-conflicts", "visual", puzzle, values.map { Cell(it) }, mode = SOLO)
        compose.setContent { SudokuTogetherTheme {
            SudokuApp(GameUiState(page = Page.PLAY, role = SOLO, snapshot = snapshot,
                selected = 16), polishActions(), {})
        } }
        // SudokuApp's entry review is verified separately below using the actual dialog.
        compose.onNodeWithTag("board_frame").assertExists()
        capture("09-gameplay")
    }

    @Test fun focusedCoach() {
        val clues = List(81) { 0 }.toMutableList().apply { this[15] = 9; this[28] = 4 }
        val values = clues.toMutableList().apply { this[16] = 9; this[31] = 4 }
        val puzzle = Puzzle("visual-coach", clues, "EASY")
        val snapshot = Snapshot("visual-coach", "visual", puzzle, values.map { Cell(it) }, mode = SOLO)
        val check = SudokuAssistance.EntryCheck(SudokuAssistance.CheckKind.CONFLICT, 16, setOf(15,16))
        compose.setContent { SudokuTogetherTheme {
            PuzzleCoachDialog(GameUiState(page = Page.PLAY, role = SOLO, snapshot = snapshot,
                selected = 16, entryCheck = check), polishActions())
        } }
        compose.onNodeWithTag("coach_entry_review_board").assertExists()
        compose.onNodeWithTag("coach_cell_15").assertExists()
        capture("10-focused-coach")
    }
}

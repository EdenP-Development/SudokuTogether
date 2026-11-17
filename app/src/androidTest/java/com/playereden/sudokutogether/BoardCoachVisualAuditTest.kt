package com.playereden.sudokutogether

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.playereden.sudokutogether.core.*
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.File

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
        val image = requireNotNull(InstrumentationRegistry.getInstrumentation().uiAutomation.takeScreenshot())
        val dir = requireNotNull(target.getExternalFilesDir("beta12-visual-review"))
        check(dir.exists() || dir.mkdirs())
        val output = File(dir, "$name.png")
        output.outputStream().use {
            assertTrue("Unable to encode $name", image.compress(Bitmap.CompressFormat.PNG, 100, it))
        }
        image.recycle()
        assertTrue("Screenshot missing or empty", output.length() > 10000)
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
        compose.onNodeWithTag("coach_cell_78").assertExists()
        screenshot("04-coach-deduction")
    }
}

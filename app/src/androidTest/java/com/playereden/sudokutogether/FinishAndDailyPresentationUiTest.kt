package com.playereden.sudokutogether

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.Density
import com.playereden.sudokutogether.core.*
import org.junit.Rule
import org.junit.Test

/** Verify presentation from persisted records without a backend or invented unlocks. */
class FinishAndDailyPresentationUiTest {
    @get:Rule val compose = createComposeRule()

    private fun completed(id: String, mode: String = SOLO, date: String? = null) =
        GameRecord(id, mode, "SOLVED", "puzzle", 120_000L, 3, 170_000L, "EASY", dailyDate = date)

    private fun finishedState(daily: Boolean = false): GameUiState {
        val puzzle = Puzzle("puzzle", List(81) { 0 }, "EASY")
        val record = completed("just-finished", if (daily) DAILY else SOLO, if (daily) "2026-11-17" else null)
        val saved = SoloProgress(record.id, puzzle, List(81) { Cell() }, 100L, elapsedMs = 120_000L, moves = 3, finishedAtMs = 170_000L)
        return GameUiState(page = Page.PLAY, role = SOLO, todayDate = "2026-11-17",
            dailyDate = if (daily) "2026-11-17" else null,
            snapshot = saved.snapshot(), finishedSolo = saved,
            profile = LocalPlayerData(records = listOf(record)))
    }

    @Test fun savedSoloFinishListsEarnedMilestonesAndKeepsNewPuzzle() {
        compose.setContent { SudokuTogetherTheme { SudokuApp(finishedState(), polishActions(), {}) } }
        compose.onNodeWithText("Puzzle completed").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("finish_new_rewards").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Achievement · First Grid").assertExists()
        compose.onNodeWithText("Collection unlock · Rabbit").assertExists()
        compose.onNodeWithText("New puzzle").performScrollTo().assertIsDisplayed()
    }

    @Test fun finishedDailyShowsActualCompletedDaysAndBackHome() {
        compose.setContent { SudokuTogetherTheme { SudokuApp(finishedState(daily = true), polishActions(), {}) } }
        compose.onNodeWithText("Daily challenge complete").performScrollTo().assertIsDisplayed()
        compose.onNodeWithTag("finish_daily_total").performScrollTo()
            .assertTextContains("1 daily puzzle completed", substring = true)
        compose.onNodeWithText("Back home").performScrollTo().assertIsDisplayed()
    }

    @Test fun homeDailyHistoryShowsRecentRecordsButCollapsesForLargeSystemText() {
        val records = listOf(completed("yesterday", DAILY, "2026-11-16"),
            completed("lastweek", DAILY, "2026-11-10"))
        val state = GameUiState(page = Page.HOME, todayDate = "2026-11-17",
            profile = LocalPlayerData(records = records))
        compose.setContent {
            SudokuTogetherTheme {
                SudokuApp(state, polishActions(), {})
            }
        }
        compose.onNodeWithTag("daily_seven_day_summary", useUnmergedTree = true)
            .assertTextContains("1 of the last 7 days completed", substring = true)
        compose.onNodeWithTag("daily_seven_day_history", useUnmergedTree = true).assertExists()
    }

    @Test fun dailyHistoryRemainsReadableAtTwoHundredPercentText() {
        val state = GameUiState(page = Page.HOME, todayDate = "2026-11-17")
        compose.setContent {
            SudokuTogetherTheme {
                CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density, 2f)) {
                    SudokuApp(state, polishActions(), {})
                }
            }
        }
        compose.onNodeWithTag("daily_seven_day_summary", useUnmergedTree = true)
            .assertTextContains("0 of the last 7 days completed", substring = true)
        compose.onNodeWithTag("daily_seven_day_history", useUnmergedTree = true).assertDoesNotExist()
    }
}

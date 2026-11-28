package com.playereden.sudokutogether

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.playereden.sudokutogether.core.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/** Runs only in the pinned Beta2 debug source on a disposable emulator. */
@RunWith(AndroidJUnit4::class)
class Beta2ToBeta3SeederTest {
    @Test fun seedHistoricalProfile() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.playereden.sudokutogether.qa", context.packageName)
        assertEquals(31, context.packageManager.getPackageInfo(context.packageName, 0).versionCode)
        val file = File(context.filesDir, "local-player.json")
        assertFalse(file.exists())
        val store = LocalPlayerStore(file)
        val digits = "123456789456789123789123456234567891567891234891234567345678912678912345912345678".map(Char::digitToInt)
        val puzzle = Puzzle("b3-upgrade-puzzle", digits.mapIndexed { i, d -> if (i == 0 || i == 1) 0 else d }, "EASY")
        val solo = PersonalPlay.newSolo(puzzle, 1000L).copy(
            id = "b3-upgrade-solo", cells = puzzle.clues.map { Cell(it) }.toMutableList().apply {
                this[0] = Cell(value = 1, revision = 1)
                this[1] = Cell(notes = 4, revision = 2)
            }, moves = 2, elapsedMs = 38000L, hintsUsed = 1,
            history = listOf(SoloUndo(0, Cell()), SoloUndo(1, Cell())))
        val records = (1..5).map { i ->
            GameRecord("b3-upgrade-record-$i", SOLO, "SOLVED", puzzle.id, 90000L, 35, i * 100000L, "EASY")
        }
        val seeded = store.update { DailyChallenge.open(it.copy(activeSolo = solo, records = records), "2026-11-17", 2000L) }
        assertEquals(5, seeded.records.size)
        assertEquals(solo, seeded.activeSolo)
        assertEquals(1, seeded.dailyProgress.size)
        assertTrue(seeded.ownedCosmeticIds.isNotEmpty())
        assertTrue(seeded.earnedAchievementIds.isNotEmpty())
        assertTrue(context.getSharedPreferences("settings", 0).edit()
            .putString("name", "Upgrade Player")
            .putString("cosmeticInk", "rose")
            .putString("cosmeticPortrait", "woodland_robin")
            .putString("difficulty", "HARD")
            .putBoolean("autoCleanNotes", true).commit())
        // Test-only reference in private files, not part of production save schema.
        File(context.filesDir, "beta2-upgrade-reference.json").writeBytes(file.readBytes())
    }
}

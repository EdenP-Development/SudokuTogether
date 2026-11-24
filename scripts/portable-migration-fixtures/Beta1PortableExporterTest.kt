package com.playereden.sudokutogether

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.playereden.sudokutogether.core.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * This file is copied into the unmodified Beta 1 source's androidTest directory
 * by the isolated upgrade-rehearsal workflow. It must compile against the OLD
 * public game model, without any Beta 2 cosmetic ownership field.
 */
@RunWith(AndroidJUnit4::class)
class Beta1PortableExporterTest {
    @Test fun exportLegacyProgress() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val storage = LocalPlayerStore(File(context.filesDir, "local-player.json"))
        val digits = "123456789456789123789123456234567891567891234891234567345678912678912345912345678"
            .map { it.digitToInt() }
        val puzzle = Puzzle("upgrade-solo-puzzle", digits.mapIndexed { index, digit ->
            if (index == 0) 0 else digit
        }, "EASY")
        val started = PersonalPlay.newSolo(puzzle, 1000L)
        val unfinished = started.copy(id = "upgrade-solo-progress",
            cells = puzzle.clues.map { Cell(it) }.toMutableList().also {
                it[0] = Cell(notes = 4, revision = 1)
            }, moves = 1, elapsedMs = 38_000L, hintsUsed = 1,
            history = listOf(SoloUndo(0, Cell())))
        val records = (1..750).map { count ->
            GameRecord("upgrade-completed-$count", SOLO, "SOLVED", puzzle.id,
                90_000L, 35, (count * 100_000L), "EASY")
        }
        val result = storage.update { old ->
            DailyChallenge.open(old.copy(activeSolo = unfinished, records = records),
                "2026-11-17", 2000L)
        }
        assertEquals(750, result.records.size)
        assertEquals(4, result.activeSolo!!.cells.first().notes)
        assertEquals(1, result.dailyProgress.size)

        assertTrue(context.getSharedPreferences("settings", 0).edit()
            .putString("name", "Upgrade Player")
            .putString("cosmeticInk", "rose")
            .putString("cosmeticPortrait", "woodland_robin")
            .putString("difficulty", "HARD")
            .putBoolean("autoCleanNotes", true).commit())
        val bytes = File(context.filesDir, "local-player.json").readText()
        assertTrue(bytes.contains("upgrade-solo-progress"))
        assertFalse("A Beta 1 profile has no newer ownership field",
            bytes.contains("ownedCosmeticIds"))
        val backup = ProgressBackupStore(File(context.filesDir, "local-player.json"), context.getSharedPreferences("settings", 0)).export(storage, BuildConfig.VERSION_NAME)
        File(context.filesDir, "migration-export.json").writeBytes(backup)
        File(context.filesDir, "migration-source-id.txt").writeText(result.playerId)
        assertTrue(backup.isNotEmpty())
    }
}

package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

class PersonalPlayTest {
    @Test fun soloNumbersNotesAndUndoDoNotEditStartingClues() {
        val game = PersonalPlay.newSolo(fixture(), 1000)
        assertNull(PersonalPlay.edit(game, 2, Kind.VALUE, 9, elapsedMs = 100))
        val notes = PersonalPlay.edit(game, 0, Kind.NOTE, 3, elapsedMs = 500)!!
        assertEquals(1 shl 2, notes.cells[0].notes)
        val reverted = PersonalPlay.edit(notes, 0, Kind.UNDO, elapsedMs = 600)!!
        assertEquals(0, reverted.cells[0].notes)
        assertTrue(reverted.history.isEmpty())
        assertEquals(600L, reverted.elapsedMs)
    }

    @Test fun soloCompletionIsSavedOnceAndBestTimeIsComputedFromResults() {
        var game = PersonalPlay.newSolo(fixture(), 1000)
        for ((cell, digit) in listOf(0 to 1, 1 to 2, 9 to 4, 10 to 5))
            game = PersonalPlay.edit(game, cell, Kind.VALUE, digit, elapsedMs = ((game.moves + 1) * 2000).toLong())!!
        assertTrue(game.completed)
        assertEquals(8000L, game.elapsedMs)
        assertEquals(4, game.moves)
        val saved = PersonalPlay.addCompletedSolo(LocalPlayerData(), game)
        val repeated = PersonalPlay.addCompletedSolo(saved, game)
        assertEquals(1, repeated.records.size)
        assertEquals(1, PersonalPlay.stats(repeated).soloWins)
        assertEquals(8000L, PersonalPlay.stats(repeated).soloBestMs)
        assertNull(PersonalPlay.edit(game, 0, Kind.CLEAR, elapsedMs = 9000))
    }

    @Test fun duplicateHistoricalCompletionIdsDoNotInflateStatistics() {
        val solo = GameRecord("solo-1", SOLO, "SOLVED", "puzzle", 8000L, 4, 9000L)
        val coop = solo.copy(id = "coop-1", mode = COOP)
        val win = solo.copy(id = "race-1", mode = VERSUS, result = "WIN")
        val loss = win.copy(id = "race-2", result = "LOSS")
        val draw = win.copy(id = "race-3", result = "DRAW")
        val unique = listOf(solo, coop, win, loss, draw)
        val data = LocalPlayerData(records = unique + unique)
        val stats = PersonalPlay.stats(data)
        assertEquals(PersonalPlay.stats(data.copy(records = unique)), stats)
        assertEquals(1, stats.soloWins)
        assertEquals(8000L, stats.soloTotalMs)
        assertEquals(1, stats.coopCompletions)
        assertEquals(1, stats.versusWins)
        assertEquals(1, stats.versusLosses)
        assertEquals(1, stats.versusDraws)
        assertEquals(10, data.records.size)
    }
}

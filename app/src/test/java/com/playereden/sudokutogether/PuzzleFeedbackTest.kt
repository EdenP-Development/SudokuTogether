package com.playereden.sudokutogether

import org.junit.Assert.assertEquals
import org.junit.Test

class PuzzleFeedbackTest {
    @Test fun playbackInvokedOnce() {
        var calls = 0
        playPuzzleClick { calls++ }
        assertEquals(1, calls)
    }

    @Test fun unavailableAudioDoesNotPreventPuzzleActions() {
        var moves = 0
        playPuzzleClick { throw IllegalStateException("Audio unavailable") }
        moves++
        assertEquals(1, moves)
    }

    @Test fun newGameStateDefaultsToSilentWithExistingTouchFeedback() {
        assertEquals(false, GameUiState().soundEffects)
        assertEquals(true, GameUiState().touchFeedback)
    }
    @Test fun cuesAreShortBoundedAndDistinct() {
        val cues = PuzzleCue.entries.map { puzzleCueSamples(it) }
        cues.forEach { samples ->
            org.junit.Assert.assertTrue(samples.size in 1..2205)
            org.junit.Assert.assertTrue(samples.all { kotlin.math.abs(it.toInt()) <= 10000 })
            assertEquals(0, samples.first().toInt())
        }
        assertEquals(cues.size, cues.map { it.contentHashCode() }.distinct().size)
    }
}

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
}

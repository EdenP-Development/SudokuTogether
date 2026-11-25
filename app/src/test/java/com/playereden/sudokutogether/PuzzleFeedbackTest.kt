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
            org.junit.Assert.assertTrue(samples.size in 1..(22050 * .75).toInt())
            org.junit.Assert.assertTrue(samples.all { kotlin.math.abs(it.toInt()) <= 10000 })
            assertEquals(0, samples.first().toInt())
        }
        assertEquals(cues.size, cues.map { it.contentHashCode() }.distinct().size)
    }
    @Test fun completionCueIsLongerThanInteractionCues() {
        assertEquals(15876, puzzleCueSamples(PuzzleCue.COMPLETE).size)
        PuzzleCue.entries.filter { it != PuzzleCue.COMPLETE }.forEach {
            org.junit.Assert.assertTrue(puzzleCueSamples(it).size <= 2205)
        }
    }
    @Test fun completionEmitsOnceForObservedActiveRound() {
        val guard = CompletionCueGuard()
        assertEquals(false, guard.observe("a:1", false))
        assertEquals(true, guard.observe("a:1", true))
        assertEquals(false, guard.observe("a:1", true))
        assertEquals(false, guard.observe("a:1", false))
        assertEquals(false, guard.observe("a:1", true))
        assertEquals(false, guard.observe("a:2", false))
        assertEquals(true, guard.observe("a:2", true))
    }
    @Test fun completedSaveOrDifferentRoundDoesNotCelebrate() {
        val guard = CompletionCueGuard()
        assertEquals(false, guard.observe("a:1", true))
        assertEquals(false, guard.observe("a:2", false))
        assertEquals(false, guard.observe("b:1", true))
        assertEquals(false, guard.observe(null, false))
        assertEquals(false, guard.observe("b:1", true))
    }
    @Test fun musicVolumeNormalization() {
        assertEquals(.25f, normalizeMusicVolume(Float.NaN), 0f)
        assertEquals(.25f, normalizeMusicVolume(Float.POSITIVE_INFINITY), 0f)
        assertEquals(0f, normalizeMusicVolume(-1f), 0f)
        assertEquals(1f, normalizeMusicVolume(2f), 0f)
        assertEquals(.42f, normalizeMusicVolume(.42f), 0f)
    }
    @Test fun restoresTrackByIdentityNotShufflePosition() {
        assertEquals(2, restoredTrackIndex(listOf("main", "evening", "final"), "final"))
        assertEquals(1, restoredTrackIndex(listOf("main", "final", "evening"), "final"))
        assertEquals(0, restoredTrackIndex(listOf("main", "evening"), "missing"))
        assertEquals(0, restoredTrackIndex(emptyList(), null))
    }
}

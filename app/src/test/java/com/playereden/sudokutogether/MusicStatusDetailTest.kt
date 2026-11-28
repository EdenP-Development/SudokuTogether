package com.playereden.sudokutogether

import org.junit.Assert.*
import org.junit.Test

class MusicStatusDetailTest {
    @Test fun routineMessagesAreAlreadyRepresentedInThePanel() {
        for (message in listOf("Playing", "Preparing music…", "Music stopped.", "Soundtrack ready.")) {
            assertFalse(message, showMusicStatusDetail(true, message))
        }
        assertFalse(showMusicStatusDetail(true, "6 tracks ready."))
        assertFalse(showMusicStatusDetail(false, "Choose the soundtrack files."))
        assertFalse(showMusicStatusDetail(false, "Soundtrack unavailable."))
    }

    @Test fun actionableMessagesRemainVisible() {
        assertTrue(showMusicStatusDetail(true, "Music interrupted. Tap Play to retry."))
        assertTrue(showMusicStatusDetail(true, "Unable to play this track."))
        assertTrue(showMusicStatusDetail(false, "Unexpected playback error."))
    }
}

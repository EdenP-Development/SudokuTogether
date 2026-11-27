package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

class CosmeticPacingTest {
    private fun assistedEasyRecords(count: Int) = (1..count).map {
        GameRecord("pacing-$it", SOLO, "SOLVED", "puzzle-$it", 900000L, 40,
            it.toLong(), difficulty = "EASY", assisted = true)
    }

    @Test fun casualSoloUnlockCurvePreservesExistingMilestones() {
        val expected = mapOf(0 to 10, 1 to 14, 2 to 17, 3 to 19, 5 to 26,
            10 to 35, 20 to 48, 50 to 62, 75 to 69, 100 to 73, 135 to 74,
            160 to 75, 190 to 76, 230 to 77, 300 to 78, 500 to 79)
        for ((count, unlocked) in expected) {
            assertEquals("Assisted Easy Solo completions: $count", unlocked,
                Cosmetics.progressAll(assistedEasyRecords(count)).count { it.value.unlocked })
        }
    }

    @Test fun multiplayerExclusiveRewardsRemainReachableWithoutChangingSoloThresholds() {
        val solo = assistedEasyRecords(500)
        val locked = Cosmetics.progressAll(solo).filterValues { !it.unlocked }.keys
        assertEquals(setOf("violet", "circuit", "mono", "duo", "flash", "laurel"), locked)
        val coop = (1..2).map {
            GameRecord("coop-pacing-$it", COOP, "SOLVED", "coop", 900000L, 40, it.toLong())
        }
        val wins = (1..3).map {
            GameRecord("race-pacing-$it", VERSUS, "WIN", "race", 900000L, 40, it.toLong())
        }
        assertEquals(Cosmetics.knownIds, Cosmetics.progressAll(solo + coop + wins)
            .filterValues { it.unlocked }.keys)
    }
}

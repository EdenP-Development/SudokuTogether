package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

class Beta3RewardContinuityTest {
    @Test fun catalogCountsAndRewardReferencesStayIntact() {
        assertEquals(85, Cosmetics.knownIds.size)
        assertEquals(47, Achievements.knownIds.size)
        val medals = Achievements.progress(emptyList())
        assertEquals(47, medals.size)
        assertTrue(medals.all { it.target > 0 })
        assertTrue(medals.mapNotNull { it.rewardId }.all { id -> Cosmetics.emblems.any { it.id == id } })
    }

    @Test fun bothEarnedLedgersSurviveRecordPruningAndRepeatedPreservation() {
        val records = (1..110).map {
            GameRecord("reward-$it", SOLO, "SOLVED", "p-$it", 900000L, 35, it.toLong(),
                difficulty = "EASY", assisted = true)
        }
        fun preserve(profile: LocalPlayerData) = Cosmetics.preserveEarned(Achievements.preserveEarned(profile))
        val initial = preserve(LocalPlayerData(records = records))
        assertTrue("solo-100" in initial.earnedAchievementIds)
        assertTrue("rose" in initial.ownedCosmeticIds)
        val pruned = preserve(initial.copy(records = emptyList()))
        assertEquals(initial.earnedAchievementIds, pruned.earnedAchievementIds)
        assertEquals(initial.ownedCosmeticIds, pruned.ownedCosmeticIds)
        assertEquals(pruned, preserve(pruned))
        assertTrue(Achievements.progress(pruned.records, pruned.earnedAchievementIds)
            .single { it.id == "solo-100" }.unlocked)
        assertTrue(Cosmetics.progressAll(pruned.records, pruned.ownedCosmeticIds).getValue("rose").unlocked)
        assertFalse("clean-5" in initial.earnedAchievementIds)
    }
}

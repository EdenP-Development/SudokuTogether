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

    @Test fun legacyProfileWithoutLedgersRetroactivelyEarnsNamesAndCosmetics() {
        val legacy = kotlinx.serialization.json.Json.decodeFromString<LocalPlayerData>(
            """{"schema":1,"playerId":"legacy-player","records":[]}""")
        val records = (1..5).map {
            GameRecord("legacy-$it", SOLO, "SOLVED", "p-$it", 900000L, 35,
                it.toLong(), difficulty = "EASY", assisted = true)
        }
        val imported = legacy.copy(records = records)
        val migrated = Achievements.preserveEarned(Cosmetics.preserveEarned(imported))
        assertEquals("legacy-player", migrated.playerId)
        assertEquals(records, migrated.records)
        assertEquals(setOf("first-grid", "steady-hand"), migrated.earnedAchievementIds.toSet())
        assertTrue("rose" in migrated.ownedCosmeticIds)
        val selection = PlayerNameSelection("first.first", "middle.acorn", "last.solver")
        val label = requireNotNull(NameCollection.display(selection))
        val pruned = migrated.copy(records = emptyList())
        assertTrue(NameCollection.canEquip(selection, NameCollection.earnedIds(pruned)))
        assertEquals(label, NameCollection.normalizedProfileName(label, pruned))
        assertEquals(Appearance(ink = "rose"),
            Cosmetics.accessible(Appearance(ink = "rose"), pruned.records, pruned.ownedCosmeticIds))
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

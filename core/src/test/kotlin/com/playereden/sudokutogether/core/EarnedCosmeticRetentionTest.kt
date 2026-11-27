package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

class EarnedCosmeticRetentionTest {
    private fun solo(id: Int) = GameRecord(
        "solved-$id", SOLO, "SOLVED", "puzzle-$id", 40000L, 30, id.toLong(), "EASY"
    )

    @Test fun olderSavedRecordsGrantPermanentOwnershipWithoutChangingExistingThresholds() {
        val legacy = LocalPlayerData(records = (1..5).map(::solo))
        assertTrue(Cosmetics.allowed("rose", legacy.records))
        val migrated = Cosmetics.preserveEarned(legacy)
        assertTrue("Five solves earn Rose", "rose" in migrated.ownedCosmeticIds)
        assertTrue("Milestone unlocks are preserved", "woodland_robin" in migrated.ownedCosmeticIds)
        assertFalse("Rare items stay locked", "ocean_narwhal" in migrated.ownedCosmeticIds)
        assertEquals(migrated, Cosmetics.preserveEarned(migrated))
    }

    @Test fun removingOldMatchRecordsDoesNotRelockPreviouslyEarnedCosmetics() {
        val migrated = Cosmetics.preserveEarned(LocalPlayerData(records = (1..5).map(::solo)))
        val pruned = Cosmetics.preserveEarned(migrated.copy(records = emptyList()))
        assertEquals(migrated.ownedCosmeticIds, pruned.ownedCosmeticIds)
        assertTrue(Cosmetics.allowed("rose", pruned.records, pruned.ownedCosmeticIds))
        assertTrue(Cosmetics.progressAll(pruned.records, pruned.ownedCosmeticIds).getValue("rose").unlocked)
        assertFalse(Cosmetics.allowed("ocean_narwhal", pruned.records, pruned.ownedCosmeticIds))
    }

    @Test fun newCompletedRecordsAccumulateWithoutDuplicateOwnershipOrMultiplayerDependency() {
        val initial = Cosmetics.preserveEarned(LocalPlayerData(records = listOf(solo(1))))
        val next = Cosmetics.preserveEarned(initial.copy(records = (1..8).map(::solo)))
        assertTrue(next.ownedCosmeticIds.containsAll(initial.ownedCosmeticIds))
        assertEquals(next.ownedCosmeticIds.size, next.ownedCosmeticIds.toSet().size)
        assertTrue(Cosmetics.allowed("pencil", next.records, next.ownedCosmeticIds))
        assertTrue(Cosmetics.allowed("woodland_squirrel", next.records, next.ownedCosmeticIds))
    }

    @Test fun ownedCosmeticsCannotBeEquippedInTheWrongCategory() {
        val wrong = Appearance(ink = "woodland_owl", board = "rose", font = "linen",
            emblem = "serif", portrait = "leaf")
        val owned = listOf("rose", "linen", "serif", "leaf")
        val resolved = Cosmetics.accessible(wrong, emptyList(), owned)
        assertEquals(Appearance(), resolved)
        assertTrue(Cosmetics.valid(resolved))
        val correct = Appearance(ink = "rose", board = "linen", font = "serif", emblem = "leaf",
            portrait = "woodland_owl")
        assertEquals(correct, Cosmetics.accessible(correct, emptyList(), owned))
    }

    @Test fun emptyFreshProfileHasNoUnnecessaryLedgerWrite() {
        val fresh = LocalPlayerData(playerId = "new-player")
        assertEquals(fresh, Cosmetics.preserveEarned(fresh))
        assertTrue(Cosmetics.allowed("forest", emptyList()))
        assertTrue(Cosmetics.allowed("woodland_owl", emptyList()))
    }
}

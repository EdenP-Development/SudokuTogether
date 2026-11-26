package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test
import kotlin.random.Random

class NameCollectionTest {
    @Test fun catalogCoversEveryExistingAchievementWithoutChangingIt() {
        assertEquals(171, NameCollection.words.size)
        assertEquals(171, NameCollection.words.map { it.id }.toSet().size)
        assertEquals(Achievements.knownIds, NameCollection.words.mapNotNull { it.achievementId }.toSet())
        for (slot in NameSlot.entries) {
            assertEquals(57, NameCollection.words.count { it.slot == slot })
            assertEquals(10, NameCollection.available(slot, emptySet()).size)
            for (id in Achievements.knownIds) assertEquals(1, NameCollection.words.count { it.slot == slot && it.achievementId == id })
        }
    }

    @Test fun exactCanonicalNamesRejectArbitraryInputAndWrongSlots() {
        val name = PlayerNameSelection("first.happy", "middle.maple", "last.fox")
        assertEquals("Happy Maple Fox", NameCollection.display(name))
        assertEquals(name, NameCollection.parseCanonical("Happy Maple Fox"))
        assertEquals("Happy Fox", NameCollection.display(name.copy(middleId = null)))
        for (invalid in listOf(" happy Fox", "Happy  Fox", "Happy Fox ", "Happy Fox!", "Happy\nFox", "Happy 123", "Alex", "Fox Happy", "Happy Unknown Fox")) {
            assertNull(invalid, NameCollection.parseCanonical(invalid))
        }
        assertNull(NameCollection.display(name.copy(firstId = "last.fox")))
        assertNull(NameCollection.display(name.copy(middleId = "unknown")))
    }

    @Test fun earnedWordsUseExistingDurableAchievementsAndHistoricalRecords() {
        val selection = PlayerNameSelection("first.first", "middle.acorn", "last.solver")
        assertFalse(NameCollection.canEquip(selection, emptySet()))
        val record = GameRecord("a", SOLO, "SOLVED", "p", 700000L, 20, 1234L, assisted = true)
        val profile = LocalPlayerData(records = listOf(record, record))
        val earned = NameCollection.earnedIds(profile)
        assertEquals(setOf("first-grid"), earned)
        assertTrue(NameCollection.canEquip(selection, earned))
        val preserved = Achievements.preserveEarned(profile).copy(records = emptyList())
        assertTrue(NameCollection.canEquip(selection, NameCollection.earnedIds(preserved)))
        assertEquals(11, NameCollection.available(NameSlot.FIRST, earned).size)
    }

    @Test fun randomizerUsesOnlyAvailableWordsAndSupportsBothLengths() {
        val random = Random(42)
        for (middle in listOf(false, true)) repeat(100) {
            val name = NameCollection.randomName(emptySet(), middle, random)
            assertEquals(middle, name.middleId != null)
            assertTrue(NameCollection.canEquip(name, emptySet()))
            assertEquals(name, NameCollection.parseCanonical(NameCollection.display(name)!!))
        }
    }

    @Test fun allStarterCombinationsRoundTrip() {
        val first = NameCollection.available(NameSlot.FIRST, emptySet())
        val middle = NameCollection.available(NameSlot.MIDDLE, emptySet())
        val last = NameCollection.available(NameSlot.LAST, emptySet())
        var count = 0
        for (f in first) for (m in listOf(null) + middle.map { it.id }) for (l in last) {
            val name = PlayerNameSelection(f.id, m, l.id)
            assertEquals(name, NameCollection.parseCanonical(NameCollection.display(name)!!))
            assertTrue(NameCollection.canEquip(name, emptySet()))
            count++
        }
        assertEquals(1100, count)
    }

    @Test fun entireCatalogRoundTripsWithinDisplayBudget() {
        val earned = Achievements.knownIds
        val first = NameCollection.available(NameSlot.FIRST, earned)
        val middle = NameCollection.available(NameSlot.MIDDLE, earned)
        val last = NameCollection.available(NameSlot.LAST, earned)
        var count = 0
        var longest = 0
        for (f in first) for (m in listOf(null) + middle.map { it.id }) for (l in last) {
            val name = PlayerNameSelection(f.id, m, l.id)
            val label = NameCollection.display(name)!!
            assertTrue(label.length <= NameCollection.MAX_DISPLAY_LENGTH)
            assertEquals(name, NameCollection.parseCanonical(label))
            longest = maxOf(longest, label.length)
            count++
        }
        assertEquals(188442, count)
        assertEquals(37, longest)
        assertNull(NameCollection.parseCanonical("x".repeat(NameCollection.MAX_DISPLAY_LENGTH + 1)))
    }

    @Test fun ordinarySoloAndDailyProgressCanUnlockMostWordsWithoutMultiplayer() {
        val offline = Achievements.progress(emptyList()).filter { it.category !in listOf("Together", "Versus") }.map { it.id }.toSet()
        assertEquals(34, offline.size)
        for (slot in NameSlot.entries) assertEquals(44, NameCollection.available(slot, offline).size)
    }
}

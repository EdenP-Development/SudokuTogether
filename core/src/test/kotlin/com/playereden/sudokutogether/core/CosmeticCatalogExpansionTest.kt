package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

/** Future collections can expand without changing milestone math or Co-op priorities. */
class CosmeticCatalogExpansionTest {
    private fun record(id: String, mode: String, result: String) =
        GameRecord(id, mode, result, "puzzle", 1000, 1, 1000)

    @Test fun allCatalogEntriesHaveUniqueIdsAndBulkProgressMatchesIndividualProgress() {
        val items = Cosmetics.portraits + Cosmetics.emblems +
            Cosmetics.boards + Cosmetics.inks + Cosmetics.fonts
        val ids = items.map { it.id }
        assertEquals("Catalog IDs may not collide across categories", ids.size, ids.toSet().size)

        val records = listOf(
            record("solo", SOLO, "SOLVED"),
            record("daily", DAILY, "SOLVED"),
            record("coop", COOP, "SOLVED"),
            record("versus-win", VERSUS, "WIN"),
            record("versus-loss", VERSUS, "LOSS"),
            record("unfinished", SOLO, "ABANDONED"),
            record("solo", SOLO, "SOLVED") // repeated record must not give extra rewards
        )

        for (sample in listOf(emptyList(), records.take(2), records, records + records)) {
            val bulk = Cosmetics.progressAll(sample)
            assertEquals(ids.toSet(), bulk.keys)
            for (id in ids) {
                assertEquals("Bulk progress disagrees for $id", Cosmetics.progress(id, sample), bulk[id])
                assertEquals("Availability disagrees for $id",
                    Cosmetics.allowed(id, sample), bulk.getValue(id).unlocked)
            }
            assertNull(Cosmetics.progress("unknown-asset", sample))
        }

        assertEquals(4, Cosmetics.completedPuzzles(records))
        val bulk = Cosmetics.progressAll(records)
        assertEquals(CosmeticProgress(0, 0), bulk["woodland_owl"])
        assertEquals(CosmeticProgress(4, 1), bulk["woodland_rabbit"])
        assertEquals(CosmeticProgress(4, 4), bulk["garden_ladybug"])
        assertEquals(CosmeticProgress(1, 1), bulk["serif"]) // 1 solo
        assertEquals(CosmeticProgress(1, 2), bulk["mono"]) // 1 co-op, 2 required
        assertEquals(CosmeticProgress(1, 2), bulk["violet"]) // 1 race won, 2 required
        assertEquals(CosmeticProgress(1, 2), bulk["linen"]) // 1 solo, 2 required
    }

    @Test fun sharedCoopBoardPriorityCoversEveryRegisteredBoardAndUnknownFallback() {
        val boards = Cosmetics.boards.map { it.id }
        assertEquals("classic", boards.first())
        for ((hostIndex, host) in boards.withIndex()) {
            assertEquals(host, Cosmetics.sharedCoopBoard(host, null))
            for ((guestIndex, guest) in boards.withIndex()) {
                assertEquals(
                    "Host=$host guest=$guest should use the higher-priority registered board",
                    boards[maxOf(hostIndex, guestIndex)],
                    Cosmetics.sharedCoopBoard(host, guest)
                )
            }
        }
        assertEquals("classic", Cosmetics.sharedCoopBoard("unknown", null))
        assertEquals("classic", Cosmetics.sharedCoopBoard("unknown", "unregistered"))
        assertEquals(boards.last(), Cosmetics.sharedCoopBoard("unknown", boards.last()))
    }
}

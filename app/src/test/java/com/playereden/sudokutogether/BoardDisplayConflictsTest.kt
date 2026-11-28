package com.playereden.sudokutogether

import org.junit.Assert.*
import org.junit.Test

class BoardDisplayConflictsTest {
    @Test fun focusedReviewKeepsOnlyDirectEvidenceWithoutMutatingGlobalConflicts() {
        val global = setOf(15, 16, 28, 31, 64, 67, 70)
        assertEquals(setOf(15, 16), boardDisplayConflicts(global, setOf(16), setOf(15), true))
        assertEquals(global, boardDisplayConflicts(global, setOf(16), setOf(15), false))
        assertEquals(setOf(15, 16, 28, 31, 64, 67, 70), global)
    }

    @Test fun nonConflictingProofCellsDoNotBecomeConflicts() {
        assertEquals(emptySet<Int>(), boardDisplayConflicts(setOf(1, 2), setOf(3), setOf(4), true))
    }
}

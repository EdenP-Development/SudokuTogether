package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

class NakedTriplePatternTest {
    private fun mask(vararg digits: Int) = digits.fold(0) { m, d -> m or (1 shl d) }

    @Test fun differentPairsCanFormAValidTriple() {
        val masks = listOf(mask(2,5), 0, mask(2,8), mask(5,8), mask(2,5,8,9), 0, 0, 0, 0)
        val triple = requireNotNull(HumanSudoku.nakedTriple(masks))
        assertEquals(listOf(0,2,3), triple.positions)
        assertEquals(mask(2,5,8), triple.mask)
        assertEquals(mask(9), masks[4] and triple.mask.inv())
        // Independent enumeration: all distinct assignments to the three squares
        // occupy exactly the reserved digits.
        val assignments = (1..9).flatMap { a -> (1..9).flatMap { b ->
            (1..9).map { c -> listOf(a,b,c) }
        }}.filter { choices -> choices.distinct().size == 3 &&
            choices.indices.all { i -> masks[triple.positions[i]] and mask(choices[i]) != 0 } }
        assertTrue(assignments.isNotEmpty())
        assertTrue(assignments.all { it.toSet() == setOf(2,5,8) })
    }

    @Test fun fourDistinctDigitsDoNotFormATriple() {
        assertNull(HumanSudoku.nakedTriple(listOf(mask(2,5),mask(2,8),mask(5,9),mask(1,2,3,4),0,0,0,0,0)))
    }

    @Test fun noAffectedCandidateMeansNoUsefulDeduction() {
        assertNull(HumanSudoku.nakedTriple(listOf(mask(2,5),mask(2,8),mask(5,8),mask(1,3,4,6),0,0,0,0,0)))
    }

    @Test fun malformedHouseIsRejected() {
        assertNull(HumanSudoku.nakedTriple(listOf(mask(2,5))))
        assertNull(HumanSudoku.nakedTriple(List(9) { 1 }))
    }
}

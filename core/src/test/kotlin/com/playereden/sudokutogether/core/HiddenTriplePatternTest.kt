package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

class HiddenTriplePatternTest {
    private fun mask(vararg digits: Int) = digits.fold(0) { m, d -> m or (1 shl d) }

    @Test fun differentPositionPairsReserveThreeDigits() {
        val masks = listOf(mask(1,2,5,9), mask(2,7,8), mask(5,7,9), mask(1,3,4,6,8,9), mask(1,3,4,6,8,9), mask(1,3,4,6,8,9),0,0,0)
        val triple = requireNotNull(HumanSudoku.hiddenTriple(masks))
        assertEquals(listOf(0,1,2), triple.positions)
        assertEquals(mask(2,5,7), triple.mask)
        assertEquals(listOf(mask(2,5),mask(2,7),mask(5,7)), triple.positions.map { masks[it] and triple.mask })
        val assignments = (1..9).flatMap { a -> (1..9).flatMap { b -> (1..9).map { c -> listOf(a,b,c) } } }
            .filter { it.distinct().size == 3 && it.indices.all { i -> masks[triple.positions[i]] and mask(it[i]) != 0 } }
            .filter { it.containsAll(listOf(2,5,7)) }
        assertTrue(assignments.isNotEmpty())
        assertTrue(assignments.all { it.toSet() == setOf(2,5,7) })
    }

    @Test fun fourthPositionInvalidatesPattern() {
        assertNull(HumanSudoku.hiddenTriple(listOf(mask(2,5,9),mask(2,7,8),mask(5,7,9),mask(7,1,3,4,6,8,9),0,0,0,0,0)))
    }

    @Test fun noExtraCandidatesMeansNoUsefulDeduction() {
        assertNull(HumanSudoku.hiddenTriple(listOf(mask(2,5),mask(2,7),mask(5,7),mask(1,3,4,6,8,9),0,0,0,0,0)))
    }

    @Test fun malformedInputRejected() {
        assertNull(HumanSudoku.hiddenTriple(List(9) { 1 }))
        assertNull(HumanSudoku.hiddenTriple(emptyList()))
    }
}

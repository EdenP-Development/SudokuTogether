package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

class NakedQuadruplePatternTest {
    private fun mask(vararg digits: Int) = digits.fold(0) { m, d -> m or (1 shl d) }

    @Test fun fourDifferentPairsReserveFourDigits() {
        val masks = listOf(mask(1,3),mask(3,6),mask(6,8),mask(1,8),mask(1,3,6,8,9),0,0,0,0)
        val quad = requireNotNull(HumanSudoku.nakedQuadruple(masks))
        assertEquals(listOf(0,1,2,3), quad.positions)
        assertEquals(mask(1,3,6,8), quad.mask)
        assertEquals(mask(9), masks[4] and quad.mask.inv())
        var assignments = 0
        for (a in 1..9) for (b in 1..9) for (c in 1..9) for (d in 1..9) {
            val digits = listOf(a,b,c,d)
            if (digits.distinct().size != 4) continue
            if (digits.indices.any { masks[quad.positions[it]] and mask(digits[it]) == 0 }) continue
            assignments++
            assertEquals(setOf(1,3,6,8), digits.toSet())
        }
        assertTrue(assignments > 0)
    }

    @Test fun fiveDigitUnionIsRejected() {
        assertNull(HumanSudoku.nakedQuadruple(listOf(mask(1,3),mask(3,6),mask(6,8),mask(1,9),
            mask(1,2,3,4,5,6,7,8,9),0,0,0,0)))
    }

    @Test fun noRemovalIsNotAUsefulStep() {
        assertNull(HumanSudoku.nakedQuadruple(listOf(mask(1,3),mask(3,6),mask(6,8),mask(1,8),
            mask(2,4,5,7,9),0,0,0,0)))
    }

    @Test fun malformedHouseRejected() {
        assertNull(HumanSudoku.nakedQuadruple(emptyList()))
        assertNull(HumanSudoku.nakedQuadruple(List(9) { 1 }))
    }
}

package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

class HiddenQuadruplePatternTest {
    private fun mask(vararg digits: Int) = digits.fold(0) { m, d -> m or (1 shl d) }

    @Test fun fourDigitsConfinedToFourDifferentPositionPairs() {
        val outside = mask(2,4,5,7,9)
        val masks = listOf(mask(1,3,2),mask(3,6,4),mask(6,8,5),mask(1,8,7),
            outside,outside,outside,outside,outside)
        val quad = requireNotNull(HumanSudoku.hiddenQuadruple(masks))
        assertEquals(listOf(0,1,2,3), quad.positions)
        assertEquals(mask(1,3,6,8), quad.mask)
        assertEquals(listOf(mask(1,3),mask(3,6),mask(6,8),mask(1,8)),
            quad.positions.map { masks[it] and quad.mask })
        assertEquals(listOf(mask(2),mask(4),mask(5),mask(7)),
            quad.positions.map { masks[it] and quad.mask.inv() })
        // Independent exhaustive assignments of the reserved digits.
        var assignments = 0
        for (a in 1..9) for (b in 1..9) for (c in 1..9) for (d in 1..9) {
            val digits = listOf(a,b,c,d)
            if (digits.toSet() != setOf(1,3,6,8)) continue
            if (digits.indices.any { masks[quad.positions[it]] and mask(digits[it]) == 0 }) continue
            assignments++
        }
        assertEquals(2, assignments)
    }

    @Test fun fifthPositionInvalidatesPattern() {
        val outside = mask(2,4,5,7,9)
        assertNull(HumanSudoku.hiddenQuadruple(listOf(mask(1,3,2),mask(3,6,4),
            mask(6,8,5),mask(1,8,7),outside or mask(8),outside,outside,outside,outside)))
    }

    @Test fun alreadyRestrictedSquaresDoNotNeedElimination() {
        val outside = mask(2,4,5,7,9)
        assertNull(HumanSudoku.hiddenQuadruple(listOf(mask(1,3),mask(3,6),
            mask(6,8),mask(1,8),outside,outside,outside,outside,outside)))
    }

    @Test fun malformedInputRejected() {
        assertNull(HumanSudoku.hiddenQuadruple(emptyList()))
        assertNull(HumanSudoku.hiddenQuadruple(List(9) { 1 }))
    }
}

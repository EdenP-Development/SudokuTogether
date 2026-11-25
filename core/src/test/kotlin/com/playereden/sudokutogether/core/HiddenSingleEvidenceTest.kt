package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

class HiddenSingleEvidenceTest {
    @Test fun physicalReviewPuzzleExplainsEveryAlternativeInRowSeven() {
        val grid = "001000080000001400600983020020000000005804210300009000006000050070000098008050300".map(Char::digitToInt)
        val hint = requireNotNull(SudokuAssistance.nextCoachHint(Puzzle("coach-review1", grid), grid))
        val step = hint.steps.first()
        assertEquals(HumanSudoku.Technique.HIDDEN_SINGLE, step.technique)
        assertEquals(listOf(59), step.targetCells)
        assertEquals(listOf(8), step.digits)
        assertEquals((54..62).toList(), step.placementGroup)
        assertEquals((54..62).filter { it != 59 }, step.placementExclusions.map { it.cell })
        for (exclusion in step.placementExclusions) {
            when (exclusion.kind) {
                SudokuAssistance.ExclusionKind.OCCUPIED -> assertTrue(grid[exclusion.cell] != 0)
                SudokuAssistance.ExclusionKind.PEER_DIGIT -> {
                    val blocker = requireNotNull(exclusion.blocker)
                    assertEquals(8, grid[blocker])
                    assertTrue(SudokuAssistance.isPeer(exclusion.cell, blocker))
                }
                SudokuAssistance.ExclusionKind.EARLIER_DEDUCTION -> fail("Initial position has direct evidence")
            }
        }
        assertEquals(2, step.placementExclusions.count { it.kind == SudokuAssistance.ExclusionKind.OCCUPIED })
    }
}

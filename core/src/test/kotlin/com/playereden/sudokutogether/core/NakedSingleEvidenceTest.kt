package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

class NakedSingleEvidenceTest {
    @Test fun allEightExcludedDigitsHaveVisiblePeerEvidence() {
        val lesson = CoachPractice.lessons.single { it.id == "only-digit" }
        val step = lesson.step
        val grid = lesson.puzzle.clues
        assertEquals(HumanSudoku.Technique.NAKED_SINGLE, step.technique)
        assertEquals(listOf(5), step.digits)
        assertEquals((1..9).filter { it != 5 }, step.digitExclusions.map { it.digit })
        for (excluded in step.digitExclusions) {
            val blocker = requireNotNull(excluded.blocker)
            assertEquals(excluded.digit, grid[blocker])
            assertTrue(SudokuAssistance.isPeer(step.targetCells.single(), blocker))
            assertNull(excluded.earlierStep)
        }
    }

    @Test fun everyNakedSingleInPracticeTracesAccountsForAllExcludedDigits() {
        for (lesson in CoachPractice.lessons) {
            val grid = lesson.puzzle.clues
            val trace = requireNotNull(SudokuAssistance.nextCoachHint(lesson.puzzle, grid)).steps
            for ((index, step) in trace.withIndex()) {
                if (step.technique != HumanSudoku.Technique.NAKED_SINGLE) continue
                assertEquals((1..9).filter { it !in step.digits }, step.digitExclusions.map { it.digit })
                for (excluded in step.digitExclusions) {
                    val blocker = excluded.blocker
                    if (blocker != null) {
                        assertEquals(excluded.digit, grid[blocker])
                        assertTrue(SudokuAssistance.isPeer(step.targetCells.single(), blocker))
                    } else {
                        val earlier = requireNotNull(excluded.earlierStep)
                        assertTrue(earlier in 1..index)
                        assertTrue(excluded.digit in trace[earlier - 1].eliminations[step.targetCells.single()].orEmpty())
                    }
                }
            }
        }
    }
}

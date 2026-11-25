package com.playereden.sudokutogether.core

import com.playereden.sudokutogether.core.HumanSudoku.Technique.HIDDEN_QUADRUPLE
import com.playereden.sudokutogether.core.HumanSudoku.Technique.HIDDEN_TRIPLE
import com.playereden.sudokutogether.core.HumanSudoku.Technique.HIDDEN_PAIR
import com.playereden.sudokutogether.core.HumanSudoku.Technique.HIDDEN_SINGLE
import com.playereden.sudokutogether.core.HumanSudoku.Technique.LOCKED_CANDIDATE
import com.playereden.sudokutogether.core.HumanSudoku.Technique.NAKED_PAIR
import com.playereden.sudokutogether.core.HumanSudoku.Technique.NAKED_QUADRUPLE
import com.playereden.sudokutogether.core.HumanSudoku.Technique.NAKED_TRIPLE
import com.playereden.sudokutogether.core.HumanSudoku.Technique.NAKED_SINGLE
import com.playereden.sudokutogether.core.HumanSudoku.Technique.X_WING
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CoachPracticeTest {
    @Test fun lessonsPresentTenRulesInOrderUsingCoachProofs() {
        val lessons = CoachPractice.lessons
        assertEquals(listOf(
            "only-digit" to NAKED_SINGLE,
            "only-square" to HIDDEN_SINGLE,
            "locked-candidate" to LOCKED_CANDIDATE,
            "naked-pair" to NAKED_PAIR,
            "hidden-pair" to HIDDEN_PAIR,
            "naked-triple" to NAKED_TRIPLE,
            "hidden-triple" to HIDDEN_TRIPLE,
            "naked-quadruple" to NAKED_QUADRUPLE,
            "hidden-quadruple" to HIDDEN_QUADRUPLE,
            "x-wing" to X_WING
        ), lessons.map { it.id to it.technique })

        for (lesson in lessons) {
            val board = lesson.puzzle.clues
            assertEquals(lesson.id, 1, Sudoku.solutionCount(board))
            assertEquals(lesson.id, 0, board[lesson.target])
            assertEquals(lesson.id, lesson.step.targetCells.first(), lesson.target)
            val trace = SudokuAssistance.nextCoachHint(lesson.puzzle, board)!!.steps
            assertEquals(lesson.id, trace.take(lesson.setup.size + 1), lesson.setup + lesson.step)
        }
    }

    @Test fun placementPromptsOfferAllDigitsAndUseTheForcedAnswer() {
        val examples = mapOf(
            "only-digit" to Example(40, listOf(5), listOf(5)),
            "only-square" to Example(65, listOf(1, 2, 6, 8), listOf(1))
        )
        for ((id, expected) in examples) {
            val lesson = CoachPractice.lessons.single { it.id == id }
            assertEquals(id, expected.target, lesson.target)
            assertEquals(id, (1..9).toList(), lesson.options)
            assertEquals(id, expected.candidates, lesson.candidatesAt(lesson.target))
            assertEquals(id, expected.answers, lesson.correctDigits)
            assertTrue(id, lesson.setup.isEmpty())
            val trial = lesson.puzzle.clues.toMutableList().apply {
                this[lesson.target] = lesson.correctDigits.single()
            }
            assertEquals(id, 1, Sudoku.solutionCount(trial))
        }
    }

    @Test fun eliminationPromptsAcceptEveryRemovalFromTheirTarget() {
        val examples = mapOf(
            "locked-candidate" to Example(62, listOf(4, 7, 8), listOf(7)),
            "naked-pair" to Example(26, listOf(1, 4, 9), listOf(9)),
            "hidden-pair" to Example(48, listOf(2, 3, 4, 9), listOf(4, 9)),
            "naked-triple" to Example(43, listOf(1, 2, 7, 8, 9), listOf(2, 7, 8)),
            "hidden-triple" to Example(12, listOf(1, 3, 5, 6, 9), listOf(3, 5, 6)),
            "naked-quadruple" to Example(28, listOf(1,4,5), listOf(4)),
            "hidden-quadruple" to Example(66, listOf(1,6,7), listOf(7)),
            "x-wing" to Example(31, listOf(5, 8), listOf(5))
        )
        for ((id, expected) in examples) {
            val lesson = CoachPractice.lessons.single { it.id == id }
            assertEquals(id, expected.target, lesson.target)
            assertEquals(id, expected.candidates, lesson.options)
            assertEquals(id, expected.answers, lesson.correctDigits)
            for ((cell, digits) in lesson.step.eliminations) for (digit in digits) {
                assertTrue(id, digit in lesson.candidatesAt(cell))
                val trial = lesson.puzzle.clues.toMutableList().apply { this[cell] = digit }
                assertEquals(id, 0, Sudoku.solutionCount(trial))
            }
        }
    }

    @Test fun hiddenPairUsesPriorEliminationsWithoutRemovingItsOwnAnswers() {
        val lesson = CoachPractice.lessons.single { it.id == "hidden-pair" }
        assertEquals(listOf(LOCKED_CANDIDATE, LOCKED_CANDIDATE, NAKED_PAIR), lesson.setup.map { it.technique })
        assertEquals(listOf(1, 9), lesson.candidatesAt(51))
        assertEquals(listOf(1, 6, 7, 9), lesson.candidatesAt(18))
        assertEquals(listOf(1, 9), lesson.candidatesAt(6))
        assertEquals(listOf(2, 3, 4, 9), lesson.candidatesAt(48))
        assertEquals(listOf(2, 3, 4, 9), lesson.candidatesAt(50))
        assertTrue(lesson.candidatesAt(5).isEmpty())
    }

    @Test fun nakedTripleReplaysTheDiscoveredProofAndPreservesGrading() {
        val lesson = CoachPractice.lessons.single { it.id == "naked-triple" }
        assertEquals(List(3) { LOCKED_CANDIDATE }, lesson.setup.map { it.technique })
        assertEquals(listOf(34, 42, 51), lesson.step.sourceCells)
        assertEquals(listOf(2, 7, 8), lesson.step.digits)
        assertEquals(mapOf(43 to listOf(2, 7, 8), 44 to listOf(2, 8), 53 to listOf(2, 8)),
            lesson.step.eliminations)
        assertEquals(listOf(2, 7), lesson.candidatesAt(34))
        assertEquals(listOf(2, 7, 8), lesson.candidatesAt(42))
        assertEquals(listOf(2, 7, 8), lesson.candidatesAt(51))
        assertEquals(listOf(1, 9), lesson.candidatesAt(43).filterNot { it in lesson.correctDigits })
        assertEquals(listOf(LOCKED_CANDIDATE, LOCKED_CANDIDATE, LOCKED_CANDIDATE,
            NAKED_TRIPLE, LOCKED_CANDIDATE, HIDDEN_SINGLE),
            SudokuAssistance.nextCoachHint(lesson.puzzle, lesson.puzzle.clues)!!.steps.map { it.technique })
        assertEquals(HumanSudoku.Grade.BEYOND_CURRENT_LOGIC, HumanSudoku.assess(lesson.puzzle).grade)
    }

    @Test fun hiddenTripleReplaysExactEvidence() {
        val lesson = CoachPractice.lessons.single { it.id == "hidden-triple" }
        assertEquals(List(2) { LOCKED_CANDIDATE }, lesson.setup.map { it.technique })
        assertEquals(listOf(12, 30, 48), lesson.step.sourceCells)
        assertEquals(listOf(1, 7, 9), lesson.step.digits)
        assertEquals(mapOf(12 to listOf(3,5,6), 30 to listOf(3,5), 48 to listOf(3)), lesson.step.eliminations)
        assertEquals(mapOf(12 to listOf(1,3,5,6,9), 30 to listOf(1,3,5,7), 48 to listOf(3,7,9)), lesson.step.proofCandidates)
        assertEquals(listOf(1,9), lesson.candidatesAt(12).filterNot { it in lesson.correctDigits })
        assertEquals(listOf(LOCKED_CANDIDATE, LOCKED_CANDIDATE, HIDDEN_TRIPLE, LOCKED_CANDIDATE),
            SudokuAssistance.nextCoachHint(lesson.puzzle, lesson.puzzle.clues)!!.steps.take(4).map { it.technique })
        assertEquals(HumanSudoku.Grade.BEYOND_CURRENT_LOGIC, HumanSudoku.assess(lesson.puzzle).grade)
    }

    @Test fun nakedQuadrupleReplaysDiscoveredEvidence() {
        val lesson = CoachPractice.lessons.single { it.id == "naked-quadruple" }
        assertEquals(listOf(LOCKED_CANDIDATE), lesson.setup.map { it.technique })
        assertEquals(listOf(27,31,33,35), lesson.step.sourceCells)
        assertEquals(listOf(3,4,6,8), lesson.step.digits)
        assertEquals(mapOf(28 to listOf(4), 29 to listOf(3), 30 to listOf(3,8), 32 to listOf(3,4,6)),
            lesson.step.eliminations)
        assertEquals(mapOf(27 to listOf(3,4,8), 28 to listOf(1,4,5), 29 to listOf(3,5,9),
            30 to listOf(3,7,8,9), 31 to listOf(3,4,6,8), 32 to listOf(1,3,4,6,7),
            33 to listOf(4,8), 35 to listOf(3,6)), lesson.step.proofCandidates)
        assertEquals(listOf(1,5), lesson.candidatesAt(28).filterNot { it in lesson.correctDigits })
        assertEquals(HumanSudoku.Grade.BEYOND_CURRENT_LOGIC, HumanSudoku.assess(lesson.puzzle).grade)
    }

    @Test fun hiddenQuadrupleReplaysDiscoveredEvidence() {
        val lesson = CoachPractice.lessons.single { it.id == "hidden-quadruple" }
        assertTrue(lesson.setup.isEmpty())
        assertEquals(listOf(66,67,75,76), lesson.step.sourceCells)
        assertEquals(listOf(1,4,6,9), lesson.step.digits)
        assertEquals(mapOf(66 to listOf(7), 67 to listOf(2,7), 75 to listOf(3), 76 to listOf(2,3,5)), lesson.step.eliminations)
        assertEquals(mapOf(66 to listOf(1,6,7), 67 to listOf(1,2,6,7,9),
            75 to listOf(1,3,4), 76 to listOf(1,2,3,4,5,9)), lesson.step.proofCandidates)
        assertEquals(HumanSudoku.Grade.BEYOND_CURRENT_LOGIC, HumanSudoku.assess(lesson.puzzle).grade)
    }

    @Test fun additionalHiddenQuadrupleFixturesRetainValidProofs() {
        val fixtures = listOf(
            "457090138963100527812357900720010093001000700649873251000000000094030070000000319" to
                mapOf(54 to listOf(2,5), 55 to listOf(8), 57 to listOf(2,5,6), 59 to listOf(2,5,6,8)),
            "000000000026938010000002690251000940060100030304200180002496001907020060600000029" to
                mapOf(1 to listOf(7), 2 to listOf(5))
        )
        for ((encoded, expected) in fixtures) {
            val grid = encoded.map(Char::digitToInt)
            assertEquals(1, Sudoku.solutionCount(grid))
            val proof = SudokuAssistance.nextCoachHint(Puzzle("hidden-quad-regression", grid), grid)!!
            val step = proof.steps.first { it.technique == HIDDEN_QUADRUPLE }
            assertEquals(expected, step.eliminations)
            for ((cell, digits) in expected) for (digit in digits) {
                assertEquals(0, Sudoku.solutionCount(grid.toMutableList().apply { this[cell] = digit }))
            }
        }
    }

    private data class Example(val target: Int, val candidates: List<Int>, val answers: List<Int>)
}

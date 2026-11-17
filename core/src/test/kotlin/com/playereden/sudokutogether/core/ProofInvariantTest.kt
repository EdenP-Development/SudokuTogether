package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

/**
 * Regression checks for the core logic behind difficulty grading and Coach.
 *
 * Every original generated puzzle is proven unique before comparing the
 * deduction trace with its one solution. Unlike checking only technique labels,
 * these assertions guard against a future rule that places a wrong digit or
 * eliminates the actual answer while still producing a plausible explanation.
 */
class ProofInvariantTest {
    private val placementTechniques = setOf(
        HumanSudoku.Technique.NAKED_SINGLE,
        HumanSudoku.Technique.HIDDEN_SINGLE
    )

    @Test fun gradedPlacementsAgreeWithTheUniqueAnswerAcrossClueFloors() {
        for (floor in listOf(36, 29, 24)) {
            for (seed in 2026L..2041L) {
                val puzzle = Sudoku.generate(seed, floor)
                val solution = requireNotNull(Sudoku.uniqueSolutionWithin(puzzle.clues, 2_000_000)) {
                    "Unique answer was not established for floor=$floor seed=$seed"
                }
                val original = puzzle.clues.toList()
                val grade = HumanSudoku.assess(puzzle)
                assertTrue("Generated board must be unique: $floor/$seed", grade.uniqueSolution)
                assertNotEquals(HumanSudoku.Grade.INVALID, grade.grade)

                var placements = 0
                for (step in grade.steps) {
                    assertTrue(step.cells.all { it in 0..80 })
                    assertTrue(step.digits.all { it in 1..9 })
                    if (step.technique in placementTechniques) {
                        val cell = step.cells.single()
                        val digit = step.digits.single()
                        assertEquals("A logical placement changed a clue: $floor/$seed",
                            0, puzzle.clues[cell])
                        assertEquals("Incorrect ${step.technique} at cell $cell: $floor/$seed",
                            solution[cell], digit)
                        placements++
                    }
                }
                if (grade.logicallySolved) {
                    assertEquals(original.count { it == 0 }, placements)
                }
                assertEquals("Assessment must not mutate the input: $floor/$seed", original, puzzle.clues)
            }
        }
    }

    @Test fun coachNeverCrossesOffTheActualDigitAndOnlyOffersValidPlacements() {
        for (floor in listOf(36, 29, 24)) {
            for (seed in 3001L..3012L) {
                val puzzle = Sudoku.generate(seed, floor)
                val answer = requireNotNull(Sudoku.uniqueSolutionWithin(puzzle.clues, 2_000_000))
                val visible = puzzle.clues.toMutableList()

                // Walk through a few consecutive hints where a placed digit
                // advances the board. When Coach only eliminates candidates,
                // stop: the UI's proof overlay does not commit those changes.
                for (hintNumber in 0 until 4) {
                    val hint = SudokuAssistance.nextCoachHint(puzzle, visible) ?: break
                    assertTrue(hint.steps.isNotEmpty())
                    for (step in hint.steps) {
                        for ((cell, removed) in step.eliminations) {
                            assertTrue(cell in 0..80)
                            assertTrue("Coach crossed off the actual answer at cell $cell: $floor/$seed",
                                answer[cell] !in removed)
                        }
                        if (step.technique in placementTechniques ||
                            step.technique == HumanSudoku.Technique.SOLUTION_REVEAL) {
                            val cell = step.targetCells.single()
                            assertEquals("Coach recommended the wrong digit: $floor/$seed",
                                answer[cell], step.digits.single())
                        }
                    }
                    val placement = hint.steps.lastOrNull { it.technique in placementTechniques }
                    if (placement != null) {
                        val target = placement.targetCells.single()
                        assertEquals("Coach must use an editable original square", 0, puzzle.clues[target])
                        assertEquals("Coach must not overwrite an already-filled square", 0, visible[target])
                        visible[target] = placement.digits.single()
                    } else {
                        break
                    }
                }
                for (cell in 0..80) if (puzzle.clues[cell] != 0) {
                    assertEquals("Coach changed an original clue", puzzle.clues[cell], visible[cell])
                }
                assertTrue(Sudoku.conflicts(visible).isEmpty())
            }
        }
    }
}

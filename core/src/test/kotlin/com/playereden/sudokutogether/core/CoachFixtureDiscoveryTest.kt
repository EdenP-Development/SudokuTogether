package com.playereden.sudokutogether.core

import org.junit.Assume.assumeTrue
import org.junit.Assert.*
import org.junit.Test
import kotlin.random.Random

/** Opt-in research tool, not a randomized release gate. */
class CoachFixtureDiscoveryTest {
    @Test fun discoverNakedTriple() {
        assumeTrue(System.getenv("GRIDMATES_COACH_DISCOVERY") == "1")
        // External mathematical reference, research only; not a shipped lesson.
        // HoDoKu n302, Bernhard Hobiger, GFDL 1.3:
        // https://hodoku.sourceforge.net/en/show_example.php?file=n302&tech=Naked+Triple
        val reference = "390000000000000650007000349009380006000054000853000000000800100002940060400000007"
        // Build the partially solved position from the reference diagram.
        val position = listOf("390000700","000000650","507000349","049380506","601054983","853000400","900800134","002940865","400000297").joinToString("")
        for ((label, digits) in listOf("reference" to reference, "position" to position)) {
            if (digits.length != 81) continue
            val grid = digits.map(Char::digitToInt).toMutableList()
            if (!Sudoku.hasUniqueSolutionWithin(grid, 200_000)) continue
            for (move in 0 until 81) {
                val puzzle = Puzzle("reference-$label-$move", grid.toList())
                val proof = SudokuAssistance.nextCoachHint(puzzle, puzzle.clues) ?: break
                val triple = proof.steps.firstOrNull { it.technique == HumanSudoku.Technique.NAKED_TRIPLE }
                if (triple != null) {
                    for ((cell, removed) in triple.eliminations) for (digit in removed)
                        assertEquals(0, Sudoku.solutionCount(grid.toMutableList().apply { this[cell] = digit }))
                    println("COACH_REFERENCE_FOUND label=$label move=$move grid=${grid.joinToString("")}")
                    println("COACH_FIXTURE_TRACE ${proof.steps.map { it.technique }}")
                    println("COACH_FIXTURE_TRIPLE $triple")
                    break
                }
                val placement = proof.steps.last()
                if (placement.technique == HumanSudoku.Technique.SOLUTION_REVEAL || placement.eliminations.isNotEmpty()) break
                grid[placement.targetCells.single()] = placement.digits.single()
            }
        }
        val random = Random(73193)
        val solution = "534678912672195348198342567859761423426853791713924856961537284287419635345286179".map(Char::digitToInt)
        val deadline = System.nanoTime() + 90_000_000_000L
        for (attempt in 0 until 5000) {
            if (System.nanoTime() >= deadline) break
            val grid = solution.toMutableList()
            val floor = 23 + random.nextInt(8)
            var remaining = 81
            for (index in grid.indices.shuffled(random)) {
                if (remaining <= floor || System.nanoTime() >= deadline) break
                val old = grid[index]
                grid[index] = 0
                if (Sudoku.hasUniqueSolutionWithin(grid, 10_000)) remaining-- else grid[index] = old
            }
            if (System.nanoTime() >= deadline) break
            for (move in 0 until 81) {
            if (System.nanoTime() >= deadline) break
            val puzzle = Puzzle("discovery-$attempt-$move", grid.toList())
            val proof = SudokuAssistance.nextCoachHint(puzzle, puzzle.clues) ?: break
            val triple = proof.steps.firstOrNull { it.technique == HumanSudoku.Technique.NAKED_TRIPLE }
            if (triple == null) {
                val placement = proof.steps.last()
                if (placement.technique == HumanSudoku.Technique.SOLUTION_REVEAL || placement.eliminations.isNotEmpty()) break
                grid[placement.targetCells.single()] = placement.digits.single()
                continue
            }
            assertEquals(1, Sudoku.solutionCount(puzzle.clues))
            for ((cell, digits) in triple.eliminations) for (digit in digits) {
                assertEquals(0, Sudoku.solutionCount(puzzle.clues.toMutableList().apply { this[cell] = digit }))
            }
            println("COACH_FIXTURE_FOUND attempt=$attempt grid=${grid.joinToString("")}")
            println("COACH_FIXTURE_TRACE ${proof.steps.map { it.technique }}")
            println("COACH_FIXTURE_TRIPLE $triple")
            return
            }
        }
        println("COACH_FIXTURE_NOT_FOUND within bounded search")
    }
}

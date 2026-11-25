package com.playereden.sudokutogether.core

import org.junit.Assume.assumeTrue
import org.junit.Assert.*
import org.junit.Test
import kotlin.random.Random

/** Opt-in research tool, not a randomized release gate. */
class CoachFixtureDiscoveryTest {
    @Test fun discoverNakedTriple() {
        assumeTrue(System.getenv("GRIDMATES_COACH_DISCOVERY") == "1")
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
            val puzzle = Puzzle("discovery-$attempt", grid.toList())
            val proof = SudokuAssistance.nextCoachHint(puzzle, puzzle.clues) ?: continue
            val triple = proof.steps.firstOrNull { it.technique == HumanSudoku.Technique.NAKED_TRIPLE } ?: continue
            assertEquals(1, Sudoku.solutionCount(puzzle.clues))
            for ((cell, digits) in triple.eliminations) for (digit in digits) {
                assertEquals(0, Sudoku.solutionCount(puzzle.clues.toMutableList().apply { this[cell] = digit }))
            }
            println("COACH_FIXTURE_FOUND attempt=$attempt grid=${grid.joinToString("")}")
            println("COACH_FIXTURE_TRACE ${proof.steps.map { it.technique }}")
            println("COACH_FIXTURE_TRIPLE $triple")
            return
        }
        println("COACH_FIXTURE_NOT_FOUND within bounded search")
    }
}

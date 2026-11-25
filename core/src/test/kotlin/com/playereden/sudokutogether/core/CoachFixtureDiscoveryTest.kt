package com.playereden.sudokutogether.core

import org.junit.Assume.assumeTrue
import org.junit.Assert.*
import org.junit.Test
import kotlin.random.Random

/** Opt-in research tool, not a randomized release gate. */
class CoachFixtureDiscoveryTest {
    @Test fun discoverTechnique() {
        assumeTrue(System.getenv("GRIDMATES_COACH_DISCOVERY") == "1")
        val target = HumanSudoku.Technique.valueOf(System.getenv("GRIDMATES_COACH_TECHNIQUE") ?: "NAKED_TRIPLE")
        val seed = System.getenv("GRIDMATES_COACH_SEED")?.toIntOrNull() ?: 73193
        val random = Random(seed)
        val solution = "534678912672195348198342567859761423426853791713924856961537284287419635345286179".map(Char::digitToInt)
        val diverse = System.getenv("GRIDMATES_COACH_DIVERSE") == "1"
        val deadline = System.nanoTime() + if (diverse) 150_000_000_000L else 90_000_000_000L
        for (attempt in 0 until if (diverse) 12000 else 5000) {
            if (System.nanoTime() >= deadline) break
            val grid = if (diverse) randomSolution(random).toMutableList() else solution.toMutableList()
            val floor = if (diverse) 21 + random.nextInt(7) else 23 + random.nextInt(8)
            var remaining = 81
            val order = grid.indices.shuffled(random)
            // Larger hidden subsets are less likely to be preempted by their
            // smaller naked complement in a nearly empty house.
            val house = if (diverse) random.nextInt(27) else 0
            fun inHouse(i: Int): Boolean = when (house) {
                in 0..8 -> i / 9 == house
                in 9..17 -> i % 9 == house - 9
                else -> i / 27 * 3 + i % 9 / 3 == house - 18
            }
            val removalOrder = if (diverse && attempt % 2 == 0)
                order.sortedBy { if (inHouse(it)) 0 else 1 } else order
            for (index in removalOrder) {
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
            val deduction = proof.steps.firstOrNull { it.technique == target }
            if (deduction == null) {
                val placement = proof.steps.last()
                if (placement.technique == HumanSudoku.Technique.SOLUTION_REVEAL || placement.eliminations.isNotEmpty()) break
                grid[placement.targetCells.single()] = placement.digits.single()
                continue
            }
            assertEquals(1, Sudoku.solutionCount(puzzle.clues))
            for ((cell, digits) in deduction.eliminations) for (digit in digits) {
                assertEquals(0, Sudoku.solutionCount(puzzle.clues.toMutableList().apply { this[cell] = digit }))
            }
            println("COACH_FIXTURE_FOUND technique=$target seed=$seed attempt=$attempt grid=${grid.joinToString("")}")
            println("COACH_FIXTURE_TRACE ${proof.steps.map { it.technique }}")
            println("COACH_FIXTURE_DEDUCTION $deduction")
            return
            }
        }
        println("COACH_FIXTURE_NOT_FOUND technique=$target seed=$seed within bounded search")
    }
    /** Generate genuinely different completed grids, not only digit permutations. */
    private fun randomSolution(random: Random): List<Int> {
        val board = IntArray(81)
        val rows = IntArray(9)
        val cols = IntArray(9)
        val boxes = IntArray(9)
        fun box(i: Int) = i / 27 * 3 + i % 9 / 3
        fun fill(): Boolean {
            var cell = -1
            var candidates = 0
            var least = 10
            for (i in board.indices) if (board[i] == 0) {
                val mask = 0x3FE and (rows[i / 9] or cols[i % 9] or boxes[box(i)]).inv()
                val count = Integer.bitCount(mask)
                if (count == 0) return false
                if (count < least) {
                    cell = i
                    candidates = mask
                    least = count
                    if (count == 1) break
                }
            }
            if (cell == -1) return true
            val r = cell / 9
            val c = cell % 9
            val b = box(cell)
            for (digit in (1..9).filter { candidates and (1 shl it) != 0 }.shuffled(random)) {
                val bit = 1 shl digit
                board[cell] = digit
                rows[r] = rows[r] or bit
                cols[c] = cols[c] or bit
                boxes[b] = boxes[b] or bit
                if (fill()) return true
                board[cell] = 0
                rows[r] = rows[r] xor bit
                cols[c] = cols[c] xor bit
                boxes[b] = boxes[b] xor bit
            }
            return false
        }
        check(fill())
        return board.toList()
    }
}

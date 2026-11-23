package com.playereden.sudokutogether

import org.junit.Assert.*
import org.junit.Test

class VersusV8GridGeometryTest {
    @Test fun decorativeBoardIsAlwaysExactlyNineByNineAndHasFourMajorBoundaries() {
        assertEquals(10, versusV8GridPositions.size)
        assertEquals(listOf(0, 3, 6, 9), versusV8MajorLines)
        assertEquals(0f, versusV8GridPositions.first())
        assertEquals(1f, versusV8GridPositions.last())
        assertEquals(1f / 3f, versusV8GridPositions[3])
        assertEquals(2f / 3f, versusV8GridPositions[6])
        assertTrue(versusV8GridPositions.zipWithNext().all { (a, b) -> b > a })
    }
}

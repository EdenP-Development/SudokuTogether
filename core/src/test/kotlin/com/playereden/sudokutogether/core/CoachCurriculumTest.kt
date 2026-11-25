package com.playereden.sudokutogether.core

import org.junit.Assert.*
import org.junit.Test

class CoachCurriculumTest {
    @Test fun sectionsPreserveEveryLessonExactlyOnceInTeachingOrder() {
        val sections = CoachPractice.sections
        assertEquals(listOf("foundations", "pairs", "larger-subsets", "advanced-patterns"), sections.map { it.id })
        assertEquals(listOf(3, 2, 4, 1), sections.map { it.lessons.size })
        val ordered = sections.flatMap { it.lessons }
        assertEquals(CoachPractice.lessons.map { it.id }, ordered.map { it.id })
        assertEquals(10, ordered.map { it.id }.distinct().size)
        assertTrue(ordered.zip(CoachPractice.lessons).all { (a,b) -> a === b })
    }
}

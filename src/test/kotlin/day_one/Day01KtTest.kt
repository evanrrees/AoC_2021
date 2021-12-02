package day_one

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class Day01KtTest {

    private val puzzleInput = File("src/test/resources/Day01.txt")

    @Test
    fun part1() {
        val result = part1(puzzleInput)
        assertEquals(7, result)
    }

    @Test
    fun part2() {
        val result = part2(puzzleInput)
        assertEquals(5, result)
    }

}
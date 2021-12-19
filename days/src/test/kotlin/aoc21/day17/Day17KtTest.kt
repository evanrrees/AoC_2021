package aoc21.day17

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day17KtTest {

    val inputFile = File("src/test/resources/Day17.txt")
    val parsedInput = parseInput(inputFile)

    @Test
    fun part1() {
        val expect = 45
        val actual = part1(parsedInput)
        assertEquals(expect, actual)
    }

    @Test
    fun part2() {
        val expect = 112
        val actual = part2(parsedInput)
        assertEquals(expect, actual)
    }
}

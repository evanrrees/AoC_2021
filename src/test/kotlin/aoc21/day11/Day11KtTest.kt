package aoc21.day11

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day11KtTest {

    val inputFile = File("src/test/resources/Day11.txt")
    val parsedInput = parseInput(inputFile)

    @Test
    fun part1_10() {
        val expect = 204L
        val steps = 10
        val actual = part1(
            octos = parsedInput,
            steps = steps,
            instructionFile = File("src/test/resources/day11/instructions.csv"),
            frameDir = "src/test/resources/day11/frames",
            verbose = true
        )
        assertEquals(expect, actual)
    }

    @Test
    fun part1() {
        val expect = 1656L
        val actual = part1(parsedInput)
        assertEquals(expect, actual)
    }

    @Test
    fun part2() {
        val expect = 195L
        val actual = part2(parsedInput)
        assertEquals(expect, actual)
    }
}

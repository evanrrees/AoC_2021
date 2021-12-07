package aoc21.day07

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day07KtTest {

    val inputFile = File("src/test/resources/Day07.txt")
    val parsedInput = parseInput(inputFile)

    @Test
    fun part1() {
        val result = part1(parsedInput)
        assertEquals(37, result)
    }

    @Test
    fun part2() {
        val result = part2(parsedInput)
        assertEquals(168, result)
    }
}
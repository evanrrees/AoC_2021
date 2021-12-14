package aoc21.day14

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day14KtTest {

    val inputFile = File("src/test/resources/Day14.txt")

    @Test
    fun part1() {
        val parsedInput = parseInput(inputFile)
        val expect = 1588
        val actual = part1(parsedInput)
        assertEquals(expect, actual)
    }

    @Test
    fun part2() {
        val parsedInput = parseInput(inputFile)
        val expect = 2188189693529L
        val actual = part2(parsedInput)
        assertEquals(expect, actual)
    }

}

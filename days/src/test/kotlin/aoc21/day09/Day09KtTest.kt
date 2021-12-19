package aoc21.day09


import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day09KtTest {

    val inputFile = File("src/test/resources/Day09.txt")
    val parsedInput = parseInput(inputFile)

    @Test
    fun part1() {
        val expect = 15
        val actual = part1(parsedInput)
        assertEquals(expect, actual)
    }

    @Test
    fun part2() {
        val expect: Int = 1134
        val actual = part2(parsedInput)
        assertEquals(expect, actual)
    }

}

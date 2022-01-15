package aoc21.day25

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day25KtTest {

    val inputFile = File("src/test/resources/Day25.txt")
    val parsedInput = parseInput(inputFile)

    @Test
    fun part1() {
        val expect = 58
        val actual = part1(parsedInput)
        assertEquals(expect, actual)
    }

    @Test
    fun part2() {
//        val expect = TODO()
//        val actual = part2(parsedInput)
//        assertEquals(expect, actual)
    }
}

package aoc21.day12

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day12KtTest {

    val smallInputFile = File("src/test/resources/day12/small.txt")
    val smallInput = parseInput(smallInputFile)
    val mediumInputFile = File("src/test/resources/day12/medium.txt")
    val mediumInput = parseInput(mediumInputFile)
    val largeInputFile = File("src/test/resources/day12/large.txt")
    val largeInput = parseInput(largeInputFile)

    @Test
    fun part1Small() {
        val expect = 10
        val actual = part1(smallInput)
        assertEquals(expect, actual)
    }

    @Test
    fun part1Medium() {
        val expect = 19
        val actual = part1(mediumInput)
        assertEquals(expect, actual)
    }

    @Test
    fun part1Big() {
        val expect = 226
        val actual = part1(largeInput)
        assertEquals(expect, actual)
    }

    @Test
    fun part2Small() {
        val expect = 36
        val actual = part2(smallInput)
        assertEquals(expect, actual)
    }

    @Test
    fun part2Medium() {
        val expect = 103
        val actual = part2(mediumInput)
        assertEquals(expect, actual)
    }

    @Test
    fun part2Big() {
        val expect = 3509
        val actual = part2(largeInput)
        assertEquals(expect, actual)
    }

}

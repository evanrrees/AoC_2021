package aoc21.day12

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day12KtTest {

    val smallInputFile = File("src/test/resources/day12/small.txt")
    val mediumInputFile = File("src/test/resources/day12/medium.txt")
    val largeInputFile = File("src/test/resources/day12/large.txt")

    @Test
    fun part1Small() {
        val expect = 10
        val actual = part1(smallInputFile)
        assertEquals(expect, actual)
    }

    @Test
    fun part1Medium() {
        val expect = 19
        val actual = part1(mediumInputFile)
        assertEquals(expect, actual)
    }

    @Test
    fun part1Big() {
        val expect = 226
        val actual = part1(largeInputFile)
        assertEquals(expect, actual)
    }

    @Test
    fun part2Small() {
        val expect = 36
        val actual = part2(smallInputFile)
        assertEquals(expect, actual)
    }

    @Test
    fun part2Medium() {
        val expect = 103
        val actual = part2(mediumInputFile)
        assertEquals(expect, actual)
    }

    @Test
    fun part2Big() {
        val expect = 3509
        val actual = part2(largeInputFile)
        assertEquals(expect, actual)
    }

}

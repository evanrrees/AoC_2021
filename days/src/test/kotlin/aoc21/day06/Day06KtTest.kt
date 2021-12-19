package aoc21.day06

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day06KtTest {

    val inputFile = File("src/test/resources/Day06.txt")
    val initialState = parseInputFile(inputFile)

    @Test
    fun part1() {
        var actual = part1(initialState, 18)
        assertEquals(26, actual)
        actual = part1(initialState, 80)
        assertEquals(5934, actual)
    }

    @Test
    fun part2() {
        val actual = part2(initialState, 256)
        assertEquals(26984457539, actual)
    }
}
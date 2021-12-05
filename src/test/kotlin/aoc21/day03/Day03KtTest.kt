package aoc21.day03

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day03KtTest {

    val inputFile = File("src/test/resources/Day03.txt")
    val input = inputFile.readLines().map { it.map(Char::digitToInt) }

    @Test
    fun part1() {
        assertEquals(198, part1(input))
    }

    @Test
    fun part2() {
        assertEquals(230, part2(input))
    }
}
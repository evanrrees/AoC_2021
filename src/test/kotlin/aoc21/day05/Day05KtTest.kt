package aoc21.day05

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day05KtTest {

    val inputFile = File("src/test/resources/Day05.txt")
    val positionList = parseInputFile(inputFile)

    @Test
    fun part1() {
        assertEquals(5, part1(positionList))
    }

    @Test
    fun part2() {
        assertEquals(12, part2(positionList))
    }
}
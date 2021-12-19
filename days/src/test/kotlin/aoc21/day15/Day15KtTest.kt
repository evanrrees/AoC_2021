package aoc21.day15

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day15KtTest {

    val inputFile = File("src/test/resources/Day15.txt")
    val parsedInput = parseInput(inputFile)

    @Test
    fun part1() {
        var expect = 40
        var actual = part1(parsedInput)
        assertEquals(expect, actual)
        expect = 315
        actual = part1(parsedInput.expand())
        assertEquals(expect, actual)
    }

    @Test
    fun expandGrid() {
        val expect = parseInput(File("src/test/resources/day15/expanded_grid.txt"))
        val actual = parseInput(inputFile).expand()
        assertEquals(expect, actual)
    }

    @Test
    fun part2() {
        var expect = 40
        var actual = part2(parsedInput)
        assertEquals(expect, actual)
        expect = 315
        actual = part2(parsedInput.expand())
        assertEquals(expect, actual)
    }

}
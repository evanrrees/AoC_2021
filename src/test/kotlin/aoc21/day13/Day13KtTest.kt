package aoc21.day13

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day13KtTest {

    val inputFile = File("src/test/resources/Day13.txt")

    @Test
    fun part1() {
        val (paper, instructions) = parseInput(inputFile)
        val expect = 17
        val actual = part1(paper, instructions.first())
        assertEquals(expect, actual)
    }

    @Test
    fun part1_2() {
        val (paper, instructions) = parseInput(inputFile)
        val expect = 16
        val result = instructions.fold(paper) { acc, it -> it.applyTo(acc) }
        assertEquals(7, result.size)
        assertEquals(5, result.first().size)

        val actual = result.sumOf { row -> row.count { it } }
        assertEquals(expect, actual)
    }

}

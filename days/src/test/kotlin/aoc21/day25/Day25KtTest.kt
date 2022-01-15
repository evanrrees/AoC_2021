package aoc21.day25

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day25KtTest {

    val inputFile = File("src/test/resources/Day25.txt")
    val grid = RollingGrid(inputFile)

    @Test
    fun part1() {
        val expect = 58
        val actual = part1(grid)
        assertEquals(expect, actual)
    }

}

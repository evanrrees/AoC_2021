package aoc21.day10

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day10KtTest {

    val inputFile = File("src/test/resources/Day10.txt")

    @Test
    fun part1() {
        val expect = 26397
        val actual = part1(inputFile.readLines())
        assertEquals(expect, actual)
    }

    @Test
    fun part2() {
        val expect = 288957L
        val actual = part2(inputFile.readLines())
        assertEquals(expect, actual)
    }
}

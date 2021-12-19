package aoc21.day04

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day04KtTest {

    val inputFile = File("src/test/resources/Day04.txt")

    @Test
    fun part1() {
        assertEquals(4512, part1(inputFile))
    }

    @Test
    fun part2() {
        assertEquals(1924, part2(inputFile))
    }
}
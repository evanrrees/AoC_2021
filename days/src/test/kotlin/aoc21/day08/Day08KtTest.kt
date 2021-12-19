package aoc21.day08

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day08KtTest {

    val inputFile = File("src/test/resources/Day08.txt")
    val parsedInput = parseInput(inputFile)

    @Test
    fun part1() {
        val expect = 26
        val actual = part1(parsedInput)
        assertEquals(expect, actual)
    }

    @Test
    fun part2() {
        val expect = 61229
        val actual = part2(parsedInput)
        assertEquals(expect, actual)
    }

    @Test
    fun simple() {
        val input = "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"
        val inputLine = input.split(" | ").let { (a, b) -> InputLine(a, b) }
        val actual = decodeDisplay(inputLine)
        val expected = 5353
        assertEquals(expected, actual) { "Expected: $expected\nActual:$actual"}
    }

    @Test
    fun encodeString() {
        val input = listOf("abcefg", "cf", "acdeg", "acdfg", "bcdf", "abdfg", "abdefg", "acf", "abcdefg", "abcdfg")
        val bytes = input.map(::encodeString)
        val actual = bytes.map { it.toString(2).padStart(7, '0') }
        val expect = listOf(
            "1110111",
            "0100100",
            "1011101",
            "1101101",
            "0101110",
            "1101011",
            "1111011",
            "0100101",
            "1111111",
            "1101111"
        )
        assertEquals(expect, actual)
    }
}

package aoc21.day18

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day18KtTest {

    val inputFile = File("src/test/resources/Day18.txt")
    val parsedInput = parseInput(inputFile)

    @Test
    fun construct() {
        val string = "[0,[[1,2],3]]"
        val actual = SnailfishNumber(string).reduce()
        assertEquals(string, actual.toString())
    }

    @Test
    fun reduce() {
        val input = SnailfishNumber("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]")
        val expect = SnailfishNumber("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")
        val actual = input.reduce()
        assertEquals(expect, actual)
    }

    @Test
    fun add() {
        val a = SnailfishNumber("[[[[4,3],4],4],[7,[[8,4],9]]]")
        val b = SnailfishNumber("[1,1]")
        val expect = SnailfishNumber("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")
        val actual = a + b
        actual.reduce()
        assertEquals(expect, actual)
    }

    @Test
    fun sums() {
        var expect = SnailfishNumber("[[[[1,1],[2,2]],[3,3]],[4,4]]")
        var actual = (1..4).map { SnailfishNumber("[$it,$it]") }.reduce { a, b -> a + b }
        assertEquals(expect, actual)

        expect = SnailfishNumber("[[[[3,0],[5,3]],[4,4]],[5,5]]")
        actual += SnailfishNumber("[5,5]")
        assertEquals(expect, actual)

        expect = SnailfishNumber("[[[[5,0],[7,4]],[5,5]],[6,6]]")
        actual += SnailfishNumber("[6,6]")
        assertEquals(expect, actual)
    }

    @Test
    fun bigSum() {
        val sumInputFile = File("src/test/resources/day18/sum_input.txt")
        val numbers = sumInputFile.readLines().map(::SnailfishNumber)
        val sumStepOutputFile = File("src/test/resources/day18/sum_output.txt")
        val expected = sumStepOutputFile.readLines().map(::SnailfishNumber)
        var actual = numbers.first()
        numbers.drop(1).zip(expected).forEach { (num, expected) ->
            actual += num
            assertEquals(expected, actual)
        }
    }

    @Test
    fun magnitude() {
        val magnitudeFile = File("src/test/resources/day18/magnitude.txt")
        magnitudeFile.readLines().forEach {
            val (lineRaw, expectRaw) = it.split(";")
            val number = SnailfishNumber(lineRaw)
            val expect = expectRaw.toInt()
            assertEquals(number.magnitude, expect)
        }
    }

    @Test
    fun part1() {
        val expected = 4140
        val actual = part1(parsedInput)
        assertEquals(expected, actual)
    }

    @Test
    fun part2() {
        val expect = 3993
        val actual = part2(inputFile.readLines())
        assertEquals(expect, actual)
    }
}

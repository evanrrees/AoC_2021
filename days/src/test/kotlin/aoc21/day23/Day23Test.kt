package aoc21.day23

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day23Test {

    object Part1 {
        val inputFile = File("/Users/err87/repos/evanrr/AoC_2021/days/src/test/resources/day23/part1.txt")
        @Test
        fun runInteractive() {
            runInteractive(inputFile)
        }

        @Test
        fun solve() {
            val expect = 12521L
            val actual = solve(inputFile)
            assertEquals(expect, actual)
        }
    }


    object Part2 {
        val inputFile = File("/Users/err87/repos/evanrr/AoC_2021/days/src/test/resources/day23/part2.txt")
        @Test
        fun runInteractive() {
            runInteractive(inputFile)
        }

        @Test
        fun solve() {
            val expect = 44169L
            val actual = solve(inputFile)
            assertEquals(expect, actual)
        }
    }
}
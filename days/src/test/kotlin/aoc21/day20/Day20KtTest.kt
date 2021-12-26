package aoc21.day20

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day20KtTest {

    val inputFile = File("src/test/resources/Day20.txt")
    val parsedInput = parseInput(inputFile)

    @Test
    fun part1A() {
        var expect = """
            .##.##.
            #..#.#.
            ##.#..#
            ####..#
            .#..##.
            ..##..#
            ...#.#.
        """.trimIndent()
        var decomp = decompress(parsedInput.image, parsedInput.algorithm, 1)
        var actual = decomp.asString()
        assertEquals(expect, actual)
        expect = """
            .......#.
            .#..#.#..
            #.#...###
            #...##.#.
            #.....#.#
            .#.#####.
            ..#.#####
            ...##.##.
            ....###..
        """.trimIndent()
        decomp = decompress(decomp, parsedInput.algorithm, 1)
        actual = decomp.asString()
        assertEquals(expect, actual)
    }

    @Test
    fun part1() {
        val expect = 35
        val actual = part1(parsedInput)
        assertEquals(expect, actual)
    }

    @Test
    fun part2() {
        val expect = TODO()
        val actual = part2(parsedInput)
        assertEquals(expect, actual)
    }
}


/*
 . . . . . . . . . . . . . . .      . . . . . . . . . . . . . . .       . . . . . . . . . . . . . . .
 . . . . . . . . . . . . . . .      . . . . . . . . . . . . . . .       . . . . . . . . . . . . . . .
 . . . . . . . . . . . . . . .      . .[. . .]. . . . . . . . . .       . . . . . . . . . . . . . . .
 . . .[. . .]. . . . . . . . .      . .[. . .]. . . . . . . . . .       . . . . . . . . . . # . . . .
 . . .[. . .]. . . . . . . . .      . .[. .[.]# # . # # .]. . . .       . . . . # . . # . # . . . . .
 . . .[. .[#]. . # .]. . . . .      . . . .[# . . # . # .]. . . .       . . . # . # . . . # # # . . .
 . . . . .[# . . . .]. . . . .      . . . .[# # . # . . #]. . . .       . . . # . . . # # . # . . . .
 . . . . .[# # . . #]. . . . .      . . . .[# # # # . . #]. . . .       . . . # . . . . . # . # . . .
 . . . . .[. . # . .]. . . . .      . . . .[. # . . # # .]. . . .       . . . . # . # # # # # . . . .
 . . . . .[. . # # #]. . . . .      . . . .[. . # # . . #]. . . .       . . . . . # . # # # # # . . .
 . . . . . . . . . . . . . . .      . . . .[. . . # . # .]. . . .       . . . . . . # # . # # . . . .
 . . . . . . . . . . . . . . .      . . . . . . . . . . . . . . .       . . . . . . . # # # . . . . .
 . . . . . . . . . . . . . . .      . . . . . . . . . . . . . . .       . . . . . . . . . . . . . . .
 . . . . . . . . . . . . . . .      . . . . . . . . . . . . . . .       . . . . . . . . . . . . . . .
 . . . . . . . . . . . . . . .      . . . . . . . . . . . . . . .       . . . . . . . . . . . . . . .
 */

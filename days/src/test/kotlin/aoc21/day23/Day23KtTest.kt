package aoc21.day23

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day23KtTest {

    val inputFile = File("src/test/resources/Day23.txt")
    val board = parseInput(inputFile)

    @Test
    fun debug() {
//        val vertices = allVertices(board)
        Board.initStates(board)
        Board.states.size
        val winners = Board.states.filter { it.key.hasWon() }
        winners.size
    }

//    @Test
//    fun part1fs() {
//        val expect = 12521L
//        val actual = part1fs(board)
//        assertEquals(expect, actual)
//    }


    @Test
    fun part1() {
        val expect = 12521L
        val actual = part1(board)
        assertEquals(expect, actual)
    }

    @Test
    fun part2() {
        val expect = TODO()
//        val actual = part2(parsedInput)
//        assertEquals(expect, actual)
    }
}

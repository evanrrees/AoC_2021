package aoc21.day21

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day21KtTest {

    val inputFile = File("src/test/resources/Day21.txt")
    val parsedInput = parseInput(inputFile)

    @Test
    fun die0() {
        assertEquals(294, rolls.elementAt(32))
        assertEquals(103, rolls.elementAt(33))
        assertEquals(200, rolls.elementAt(66))
        assertEquals(297, rolls.elementAt(99))
        assertEquals(6, rolls.elementAt(100))
        assertEquals(15, rolls.elementAt(101))
    }

    @Test
    fun part1Small() {
        val p1 = Player(4)
        val p2 = Player(8)
        val dieit = rolls.windowed(2, 2).iterator()
        dieit.next().let { (a, b) ->
            p1.move(a)
            p2.move(b)
        }
        assertEquals(Player(10, 10), p1)
        assertEquals(Player(3, 3), p2)

        dieit.next().let { (a, b) ->
            p1.move(a)
            p2.move(b)
        }
        assertEquals(Player(4, 14), p1)
        assertEquals(Player(6, 9), p2)

        dieit.next().let { (a, b) ->
            p1.move(a)
            p2.move(b)
        }
        assertEquals(Player(6, 20), p1)
        assertEquals(Player(7, 16), p2)

        dieit.next().let { (a, b) ->
            p1.move(a)
            p2.move(b)
        }
        assertEquals(Player(6, 26), p1)
        assertEquals(Player(6, 22), p2)
    }

    @Test
    fun parseInput() {
        val expect = ParsedInput(Player(4), Player(8))
        assertEquals(expect, parsedInput)
    }

    @Test
    fun part1() {
        val expect = 739785
        val actual = part1(parsedInput)
        assertEquals(expect, actual)
    }

    @Test
    fun part2() {
        val expect = 444356092776315L
        val actual = part2(parsedInput)
        assertEquals(expect, actual)
    }
}

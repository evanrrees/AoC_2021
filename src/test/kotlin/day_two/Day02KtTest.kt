package day_two

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day02KtTest {

    private val inputFile = File("src/test/resources/Day02.txt")

    @Test
    fun part1() {
        val result = part1(inputFile)
        assertEquals(150, result)
    }

    @Test
    fun part2() {
        val result = part2(inputFile)
        assertEquals(900, result)
    }

}
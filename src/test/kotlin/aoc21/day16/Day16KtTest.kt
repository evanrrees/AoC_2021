package aoc21.day16

import aoc21.utils.split.mapSplit
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day16KtTest {

    val inputDir = File("src/test/resources/day16")
    val inputFiles = inputDir.listFiles { _, name -> name.matches("hex_[0-9]+.txt".toRegex()) }!!
        .sorted()
//    val parsedInput = parseInput(inputFile)

    init {
        assert(inputFiles.isNotEmpty())
        assert(inputFiles.size == 7)
    }

    @Test
    fun parseInput() {
        val expectParsed = listOf(
            "110100101111111000101000",
            "00111000000000000110111101000101001010010001001000000000",
            "11101110000000001101010000001100100000100011000001100000"
        )
        val actualParsed = inputFiles.map(::parseInput)
        expectParsed.zip(actualParsed) { expect, actual ->
            assertEquals(expect, actual)
        }
    }

    @Test
    fun versions() {
        val expectedVersions = listOf<Long>(6, 1, 7, 4, 3)
        expectedVersions.zip(inputFiles) { expect, file ->
            val parsedInput = parseInput(file)
            val packet = Decoder(parsedInput).decodePacket()
            assertEquals(expect, packet.version)
        }
    }

    @Test
    fun deepVersions() {
        val expectedDeepVersions = listOf<Long>(6, 9, 14, 16, 12, 23, 31)
        expectedDeepVersions.zip(inputFiles) { expect, file ->
            val parsedInput = parseInput(file)
            val packet = Decoder(parsedInput).decodePacket()
            assertEquals(expect, packet.deepVersion)
        }
    }

    @Test
    fun size() {
        val expectedSizes = listOf(1, 3, 4)
        expectedSizes.zip(inputFiles) { expect, file ->
            val parsedInput = parseInput(file)
            val packet = Decoder(parsedInput).decodePacket()
            assertEquals(expect, packet.size)
        }
    }


    @Test
    fun debuggit() {
        for (f in inputFiles) {
            val parsedInput = parseInput(f)
            val decoder = Decoder(parsedInput)
            val decoded = decoder.decodePacket()
            decoded.value
            decoded.version
        }
    }

    @Test
    fun part1() {
        val expectedSums = listOf<Long>(6, 9, 14, 16, 12, 23, 31)
        expectedSums.zip(inputFiles) { expect, file ->
            val parsedInput = parseInput(file)
            assertEquals(expect, part1(parsedInput))
        }
    }

    @Test
    fun part2() {
        val part2File = File("src/test/resources/day16/part2.txt")
        val inputs = part2File.useLines { lines ->
            lines.mapSplit(",").associate { it.first().hexToBinary() to it.last().toLong() }
        }
        inputs.forEach { (input, expected) ->
            val packet = Decoder(input).decodePacket()
            assertEquals(expected, packet.value)
        }
    }
}

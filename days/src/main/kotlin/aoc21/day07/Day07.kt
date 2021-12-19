package aoc21.day07

import utils.ranges.range
import utils.split.split
import java.io.File
import kotlin.math.abs

fun parseInput(inputFile: File) = inputFile.useLines { it.first() }.split(",") { it.toInt() }

fun part1(crabs: List<Int>) = with(crabs.sorted()) {
    get(size / 2).let { median -> sumOf { abs(median - it)  } }
}

fun cost(starts: List<Int>, pos: Int) = starts.sumOf { abs(it - pos).let { x -> x * (x + 1) / 2L } }
fun part2(crabs: List<Int>) = crabs.range()
    .map { cost(crabs, it) }
    .zipWithNext()
    .first { (a, b) -> a <= b }
    .first

fun main() {
    val inputFile = File("src/main/resources/Day07.txt")
    val parsedInput = parseInput(inputFile)
    val result1 = part1(parsedInput)
    println(result1)
    val result2 = part2(parsedInput)
    println(result2)
}
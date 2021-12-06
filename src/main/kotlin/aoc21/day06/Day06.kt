package aoc21.day06

import java.io.File
import aoc21.utils.mapSplit
import aoc21.utils.shiftRight

fun parseInputFile(inputFile: File): List<Long> = inputFile.readLines().first().mapSplit(",") { it.toLong() }

fun part1(initialState: List<Long>, days: Int): Long {
    val fish = MutableList(9) { 0L }
    initialState.groupingBy { it }.eachCount().forEach { (age, count) -> fish[(7 - age.toInt()) + 1] = count.toLong() }
    repeat(days) {
        fish.shiftRight()
        fish[2] += fish.first()
    }
    return fish.sum()
}

fun part2(initialState: List<Long>, days: Int) = part1(initialState, days)

fun main() {
    val inputFile = File("src/main/resources/Day06.txt")
    val initialState = parseInputFile(inputFile)
    val result1 = part1(initialState, 80)
    println(result1)
    val result2 = part2(initialState, 256)
    println(result2)
}
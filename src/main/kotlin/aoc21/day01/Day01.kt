package aoc21.day01

import java.io.File

fun part1(puzzleInput: File) = puzzleInput.useLines { lines ->
    lines.map(String::toInt)
        .windowed(2)
        .count { (a, b) -> b > a }
}

fun part2(puzzleInput: File) = puzzleInput.useLines { lines ->
    lines.map(String::toInt)
        .windowed(3) { it.sum() }
        .windowed(2)
        .count { (a, b) -> b > a }
}

fun main(args: Array<String>) {
    val result1 = part1(File(args[0]))
    val result2 = part2(File(args[0]))
    println(result1)
    println(result2)
}
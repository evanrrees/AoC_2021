package aoc21.day10

import java.io.File

val opening = mapOf(
    ')' to '(',
    ']' to '[',
    '}' to '{',
    '>' to '<'
)

fun part1(parsedInput: List<String>): Int {
    val points = mapOf(
        ')' to 3,       // 3
        ']' to 57,      // 3 * 19
        '}' to 1197,    // 3 * 3 * 7 * 19
        '>' to 25137    // 3 * 3 * 3 * 7 * 7 * 19
    )
    var total = 0
    for (line in parsedInput) {
        if (line.first() in opening.keys)
            total += points.getValue(line.first())
        else {
            val stack = mutableListOf(line.first())
            for (c in line.drop(1))
                if (c in opening.values)
                    stack.add(c)
                else if (opening.getValue(c) == stack.last())
                    stack.removeLast()
                else {
                    total += points.getValue(c)
                    break
                }
        }
    }
    return total
}

fun part2(parsedInput: List<String>): Long {
    val scores = mutableListOf<Long>()
    val points = opening.values
    parsedInput.forEach { line ->
        if (line.first() in opening.keys) return@forEach
        val stack = mutableListOf(line.first())
        for (c in line.drop(1))
            if (c in opening.values)
                stack.add(c)
            else if (opening.getValue(c) == stack.last())
                stack.removeLast()
            else {
                return@forEach
            }
        val score = stack.foldRight(0L) { it, acc -> acc * 5 + points.indexOf(it) + 1 }
        if (score != 0L) scores.add(score)
    }
    return scores.sorted()[scores.size / 2]
}

fun main() {
    val inputFile = File("src/main/resources/Day10.txt")
    val lines = inputFile.readLines()
    val result1 = part1(lines)
    println(result1)
    val result2 = part2(lines)
    println(result2)
}

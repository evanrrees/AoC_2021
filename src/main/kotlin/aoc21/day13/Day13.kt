package aoc21.day13

import java.io.File
import aoc21.utils.konsole.format
import aoc21.utils.konsole.reset

data class Instruction(val vertical: Boolean, val value: Int) {
    val horizontal get() = !vertical
    constructor(strings: List<String>): this(strings.first() == "x", strings.last().toInt())
    constructor(string: String): this(string.substringAfterLast(' ').split('='))
    fun applyTo(paper: Paper) = if (vertical) foldVertical(paper, value) else foldHorizontal(paper, value)
}

data class Point(val x: Int, val y: Int) {
    constructor(ints: List<Int>): this(ints[0], ints[1])
    constructor(string: String): this(string.split(",").map(String::toInt))
}

typealias Paper = Array<Array<Boolean>>

fun Paper.print() = forEach { row ->
    row.joinToString("") { " ".format { background; if (it) { white } else { black } }.reset() }.let(::println)
}

fun fillPaper(points: List<Point>): Array<Array<Boolean>> {
    val paper = Array(points.maxOf { it.y } + 1) { Array(points.maxOf { it.x } + 1) { false } }
    points.forEach { (j, i) -> paper[i][j] = true }
    return paper
}

fun foldHorizontal(paper: Paper, at: Int): Paper {
    val height = if (at < paper.size / 2) paper.lastIndex - at else at
    val width = paper.first().size
    val result = Paper(height) { i ->
        Array(width) { j ->
            paper.getOrNull(at - height + i)?.get(j) ?: false || paper.getOrNull(at + height - i)?.get(j) ?: false
        }
    }
    return result
}

fun foldVertical(paper: Paper, at: Int): Paper {
    val width = if (at < paper.first().size / 2) paper.first().lastIndex - at else at
    val result = Paper(paper.size) { i ->
        Array(width) { j ->
            paper[i].getOrNull(at - width + j) ?: false || paper[i].getOrNull(at + width - j) ?: false
        }
    }
    return result
}

fun part1(paper: Paper, instruction: Instruction) = instruction.applyTo(paper).sumOf { row -> row.count { it } }

fun part2(paper: Paper, instructions: List<Instruction>): Int {
    val result: Paper = instructions.fold(paper) { acc, it -> it.applyTo(acc) }
    result.print()
    return 0
}


fun parseInput(inputFile: File): Pair<Paper, List<Instruction>> {
    val (points, instructions) = inputFile.useLines { lines ->
        lines.filter(String::isNotBlank).partition { it.first().isDigit() }
    }
    return fillPaper(points.map(::Point)) to instructions.map(::Instruction)
}

fun main() {
    val inputFile = File("src/main/resources/Day13.txt")
    val (paper, instructions) = parseInput(inputFile)
    val result1 = part1(paper, instructions.first())
    println(result1)
    val result2 = part2(paper, instructions)

}

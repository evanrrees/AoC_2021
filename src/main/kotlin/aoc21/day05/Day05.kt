package aoc21.day05

import aoc21.utils.expand
import aoc21.utils.progressionTo
import aoc21.utils.toPair
import java.io.File


fun countIntersections(lines: List<Line>) =
    Array(lines.maxOf(Line::xMax) + 1) {
        Array(lines.maxOf(Line::yMax) + 1) { 0 }
    }
        .apply { lines.forEach { it.drawTo(this) } }
        .sumOf { row -> row.count { it > 1 } }

fun part1(positionList: List<Line>) = countIntersections(positionList.filter { !it.isDiagonal })
fun part2(positionList: List<Line>) = countIntersections(positionList)

typealias Point = Pair<Int, Int>
val Point.x get() = first
val Point.y get() = second

typealias Line = Pair<Point, Point>
val Line.start          get() = first
val Line.end            get() = second
val Line.isDiagonal     get() = start.y != end.y && start.x != end.x
val Line.xRange         get() = start.x progressionTo end.x
val Line.yRange         get() = start.y progressionTo end.y
val Line.xMax           get() = maxOf(start.x, end.x)
val Line.yMax           get() = maxOf(start.y, end.y)
fun Line.points() = if (isDiagonal) yRange zip xRange else yRange expand xRange
fun Line.drawTo(array: Array<Array<Int>>) = points().forEach { array[it.y][it.x]++ }

fun parseInputFile(inputFile: File): List<Line> = inputFile.readLines().map(::parseLine)
fun parseLine(line: String): Line = line.split(" -> ").map(::parsePoint).let(List<Point>::toPair)
fun parsePoint(string: String): Point = string.split(",").map(String::toInt).let(List<Int>::toPair)

fun main() {
    val positions = parseInputFile(File("src/main/resources/Day05.txt"))
    println(part1(positions))
    println(part2(positions))
}
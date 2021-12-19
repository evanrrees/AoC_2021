package aoc21.day17

import utils.geom.Rectangle
import utils.grids.Point
import utils.ranges.expand
import utils.split.split
import utils.timeit
import java.io.File
import kotlin.math.abs


class Velocity(x: Int, y: Int): Point(i = y, j = x) {
    fun next() = Velocity(x = maxOf(x - 1, 0), y = y - 1)
    fun hits(t: Rectangle, p: Point = Point(0, 0)): Boolean = when {
        p in t -> true
        p.x > t.xRange.last -> false
        p.y < t.yRange.first -> false
        else -> next().hits(t, p + this)
    }
}

tailrec fun minVx(t: Int, sum: Int = 0, x: Int = 1): Int = if (sum + x >= t) x else minVx(t, sum + x, x + 1)

fun part1(target: Rectangle) = abs(target.yRange.first).let { it * (it - 1) / 2 }

fun part2(target: Rectangle): Int {
    val xMin = minVx(target.xRange.first)
    val xMax = target.xRange.last
    val yMin = target.yRange.first
    val yMax = abs(target.yRange.first)
    return (xMin..xMax).expand(yMin..yMax).count { (x, y) -> Velocity(x, y).hits(target) }
}

fun parseInput(inputFile: File): Rectangle {
    val line = inputFile.readLines().single()
    val (x0, x1) = line.substringAfter("x=").substringBefore(",").split("..") { it.toInt() }
    val (y0, y1) = line.substringAfter("y=").substringBefore(",").split("..") { it.toInt() }
    return Rectangle(x0, x1, y0, y1)
}

fun main() {
    val inputFile = File("src/main/resources/Day17.txt")
    val parsedInput = parseInput(inputFile)
    timeit("Part 1:") { part1(parsedInput) }
    timeit("Part 2:") { part2(parsedInput) }
}

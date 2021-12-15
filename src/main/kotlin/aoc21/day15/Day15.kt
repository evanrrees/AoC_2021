package aoc21.day15

import java.io.File
import aoc21.utils.grids.*
import aoc21.utils.timeit

typealias ParsedInput = Array<Array<Int>>
fun ParsedInput.expand(): ParsedInput {
    val height = this.size
    val width = this.map { it.size }.distinct().single()
    val result = Array(height * 5) { Array(width * 5) { 0 } }
    for (i in result.indices) {
        for (j in result[i].indices) {
            result[i][j] =
                when {
                    i < height    -> if (j < width) this[i][j] else result[i][j - width] + 1
                    j < width * 4 -> result[i - height][j + width]
                    else          -> result[i][j - width] + 1
                }
                    .let { if (it > 9) it - 9 else it }
        }
    }
    return result
}

fun part1(arr: ParsedInput): Int {
    val dp = List(arr.size) { MutableList(arr[it].size) { 0 } }
    val tb = List(arr.size) { i ->
        MutableList(arr[i].size) { j ->
            (if (i == 0) 0 else -1) to (if (j == 0) 0 else -1)
        }
    }
    tb[0][0] = 0 to 0
    for (i in 1..dp.lastIndex) dp[i][0] = arr[i][0] + dp[i - 1][0]
    for (j in 1..dp[0].lastIndex) dp[0][j] = arr[0][j] + dp[0][j - 1]
    for (i in 1..dp.lastIndex) {
        for (j in 1..dp[i].lastIndex) {
            val (m, n) = if (dp[i - 1][j] <= dp[i][j - 1]) i - 1 to j else i to j - 1
            tb[i][j] = m - i to n - j
            dp[i][j] = dp[m][n] + arr[i][j]
        }
    }
    return dp.last().last()
}

class DPoint(
    i: Int,
    j: Int,
    val risk: Int,
    var known: Boolean = false,
    var dist: Int = Int.MAX_VALUE,
    var prev: DPoint? = null
): Point(i, j) {
    fun forEachNeighborIn(grid: Grid<DPoint>, action: (DPoint) -> Unit) {
        if (i > 0)                 action(grid[i - 1][j])
        if (i < grid.lastIndex)    action(grid[i + 1][j])
        if (j > 0)                 action(grid[i][j - 1])
        if (j < grid[i].lastIndex) action(grid[i][j + 1])
    }
}

fun parseInput(inputFile: File) = inputFile.useLines { lines ->
    lines.map { line -> Array(line.length) { line[it].digitToInt() } }.toList().toTypedArray()
}

fun part2(risks: ParsedInput): Int {
    val grid = createGrid(risks.size, risks.distinctBy { it.size }.single().size) { i, j -> DPoint(i, j, risks[i][j]) }
    val heap = mutableSetOf(grid.first().first().also { it.dist = 0 })
    val iter = grid.sumOf { it.size }
    repeat(iter) {
        val v = heap.minByOrNull { it.dist }!!
        v.forEachNeighborIn(grid) { u ->
            if (!u.known) {
                val newDist = v.dist + u.risk
                if (newDist < u.dist) {
                    u.dist = newDist
                    u.prev = v
                    heap += u
                }
            }
        }
        heap.remove(v)
        v.known = true
    }
    return grid.last().last().dist
}

fun main() {
    val inputFile = File("src/main/resources/Day15.txt")
    val parsedInput = parseInput(inputFile)
    timeit("Part 1") { part2(parsedInput) }
    timeit("Part 2") { part2(parsedInput.expand()) }
}

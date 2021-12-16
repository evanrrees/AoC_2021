package aoc21.day15

import java.io.File
import aoc21.utils.grids.*
import aoc21.utils.timeit


typealias ParsedInput = Grid<Int>
fun ParsedInput.expand(): ParsedInput {
    val result = Grid(rows * 5, cols * 5, 0)
    result.forEachIndexed { i, j, _ ->
        result[i, j] =
            when {
                i < rows     -> if (j < cols) this[i, j] else result[i, j - cols] + 1
                j < cols * 4 -> result[i - rows, j + cols]
                else         -> result[i, j - cols] + 1
            }
                .let { if (it > 9) it - 9 else it }
    }
    return result
}

fun part1(arr: ParsedInput): Int {
    val dp = Grid(arr.rows, arr.cols, 0)
    val tb = Grid(arr.rows, arr.cols) { i, j ->
        (if (i == 0) 0 else -1) to (if (j == 0) 0 else -1)
    }
    tb[0, 0] = 0 to 0
    for (i in 1..dp.lastRowIndex) dp[i, 0] = arr[i, 0] + dp[i - 1, 0]
    for (j in 1..dp.lastColIndex) dp[0, j] = arr[0, j] + dp[0, j - 1]
    for (i in 1..dp.lastRowIndex) {
        for (j in 1..dp.lastColIndex) {
            val (m, n) = if (dp[i - 1, j] <= dp[i, j - 1]) i - 1 to j else i to j - 1
            tb[i, j] = m - i to n - j
            dp[i, j] = dp[m, n] + arr[i, j]
        }
    }
    return dp.last()
}

class DPoint(
    i: Int,
    j: Int,
    val risk: Int,
    var seen: Boolean = false,
    var dist: Int = Int.MAX_VALUE,
    var prev: DPoint? = null
): Point(i, j) {
    fun forEachNeighborIn(grid: Grid<DPoint>, action: (DPoint) -> Unit) {
        if (i > 0)                 action(grid[i - 1, j])
        if (i < grid.lastRowIndex) action(grid[i + 1, j])
        if (j > 0)                 action(grid[i, j - 1])
        if (j < grid.lastColIndex) action(grid[i, j + 1])
    }
}

fun parseInput(inputFile: File) = inputFile.useLines { lines -> lines.map { it.map(Char::digitToInt) }.toList() }
    .asGrid()

fun part2(risks: ParsedInput): Int {
    val grid = Grid(risks.rows, risks.cols) { i, j -> DPoint(i, j, risks[i, j]) }
    val bag = mutableSetOf(grid[0, 0].also { it.dist = 0 })
    repeat(grid.size) {
        val v = bag.minByOrNull { it.dist }!!
        v.forEachNeighborIn(grid) { u ->
            val newDist = v.dist + u.risk
            if (newDist < u.dist) {
                u.dist = newDist
                u.prev = v
                bag += u
            }
        }
        bag.remove(v)
        v.seen = true
    }
    return grid.last().dist
}

fun main() {
    val inputFile = File("src/main/resources/Day15.txt")
    val parsedInput = parseInput(inputFile)
    timeit("Part 1") { part2(parsedInput) }
    timeit("Part 2") { part2(parsedInput.expand()) }
}

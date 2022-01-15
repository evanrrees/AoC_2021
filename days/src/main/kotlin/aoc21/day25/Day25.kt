package aoc21.day25

import utils.grids.Grid
import java.io.File
import utils.timeit

internal fun <T> Array<T>.getr(index: Int): T {
    return if (index in indices) get(index)
    else if (index < 0) getr(size + (index % size))
    else get(index % size)
}

fun <T> Array<T>.getc(index: Int) =
    when (index) {
        in indices -> get(index)
        size       -> get(0)
        -1         -> get(lastIndex)
        else       -> throw NoSuchElementException("$index")
    }

internal class RollingGrid<T>(arr: Array<Array<T>>): Grid<T>(arr) {
    override operator fun get(i: Int, j: Int): T = arr.getc(i).getc(j)
    override fun toString() = arr.joinToString("\n") { it.joinToString("") }
}

internal fun step(grid: RollingGrid<Char>): RollingGrid<Char> {
    return Grid(grid.rows, grid.cols) { i, j ->
        val above      = grid[i - 1, j]
        val below      = grid[i + 1, j]
        val left       = grid[i, j - 1]
        val right      = grid[i, j + 1]
        val belowRight = grid[i + 1, j + 1]
        val belowLeft  = grid[i + 1, j - 1]
        when (val char = grid[i, j]) {
            '.' ->
                if      (left  == '>') '>'
                else if (above == 'v') 'v'
                else                   char
            '>' ->
                if      (right != '.') char
                else if (above == 'v') 'v'
                else                   '.'
            else ->
                if      (below == '.' && belowLeft  != '>') '.'
                else if (below == '>' && belowRight == '.') '.'
                else                                        char
        }
    }.let { RollingGrid(it.arr) }
}

internal fun part1(parsedInput: RollingGrid<Char>): Int {
    var prev = parsedInput
    var curr = step(parsedInput)
    var n = 1
    while (prev != curr) {
        prev = curr
        curr = step(prev)
        n++
    }
    return n
}

internal fun part2(parsedInput: Grid<Char>): Int {
    TODO()
}

internal fun parseInput(inputFile: File): RollingGrid<Char> {
    val lines = inputFile.readLines()
    return RollingGrid(Array(lines.size) { lines[it].toCharArray().toTypedArray() })
}

fun main() {
    val inputFile = File("days/src/main/resources/Day25.txt")
    val parsedInput = parseInput(inputFile)
    timeit("Part 1:") { part1(parsedInput) }
    //timeit("Part 2:") { part2(parsedInput) }
}

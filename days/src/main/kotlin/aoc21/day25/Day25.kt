package aoc21.day25

import utils.grids.Grid
import utils.grids.toString
import java.io.File
import utils.timeit

fun <T> Array<T>.getc(index: Int) =
    when (index) {
        in indices -> get(index)
        size       -> get(0)
        -1         -> get(lastIndex)
        else       -> throw NoSuchElementException("$index")
    }

internal class RollingGrid<T>(arr: Array<Array<T>>): Grid<T>(arr) {
    override operator fun get(i: Int, j: Int): T = arr.getc(i).getc(j)
    override fun toString(): String = toString("")
}

internal fun RollingGrid(file: File): RollingGrid<Char> =
    file.readLines().map { it.toCharArray().toTypedArray() }.toTypedArray().let(::RollingGrid)

internal fun RollingGrid<Char>.move() =
    Array(rows) { i ->
        Array(cols) { j ->
            val above      = this[i - 1, j]
            val below      = this[i + 1, j]
            val left       = this[i, j - 1]
            val right      = this[i, j + 1]
            val belowRight = this[i + 1, j + 1]
            val belowLeft  = this[i + 1, j - 1]
            when (val c = this[i, j]) {
                '.' ->
                    if      (left  == '>') '>'
                    else if (above == 'v') 'v'
                    else                   c
                '>' ->
                    if      (right != '.') c
                    else if (above == 'v') 'v'
                    else                   '.'
                else ->
                    if      (below == '.' && belowLeft  != '>') '.'
                    else if (below == '>' && belowRight == '.') '.'
                    else                                        c
            }
        }
    }.let(::RollingGrid)

internal tailrec fun part1(grid: RollingGrid<Char>, next: RollingGrid<Char> = grid.move(), n: Int = 1): Int =
    if (next == grid) n else part1(next, next.move(), n + 1)

fun main() {
    val inputFile = File("days/src/main/resources/Day25.txt")
    val grid = RollingGrid(inputFile)
    timeit("Part 1:") { part1(grid) }
}

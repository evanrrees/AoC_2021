package aoc21.day20

import org.tinylog.kotlin.Logger
import utils.grids.Grid
import java.io.File
import utils.timeit

data class ParsedInput(val image: Grid<Int>, val algorithm: List<Int>)

fun Grid<Int>.asString() =
    arr.joinToString("\n") { row -> row.joinToString("") { if (it == 0) "." else "#" } }

fun decompress(i0: Int, j0: Int, image: Grid<Int>, algorithm: List<Int>, inf: Int): Int {
    var result = 0
    for (i1 in (i0 - 2)..i0)
        for (j1 in (j0 - 2)..j0)
            result = result shl 1 or if (i1 in image.rowIndices && j1 in image.colIndices) image[i1, j1] else inf
    return algorithm[result]
}

tailrec fun decompress(image: Grid<Int>, algorithm: List<Int>, n: Int): Grid<Int> =
    if (n == 0) image
    else {
        val inf = if (algorithm[0] == 1) n % 2 else 0
        val decompressed = Grid(image.rows + 2, image.cols + 2) { i, j -> decompress(i, j, image, algorithm, inf) }
//        decompressed.print()
        decompress(decompressed, algorithm, n - 1)
    }

fun part1(parsedInput: ParsedInput): Int {
    val decompressed = decompress(parsedInput.image, parsedInput.algorithm, 2)
    return decompressed.arr.sumOf { row -> row.sumOf { it } }
}

fun part2(parsedInput: ParsedInput): Int {
    val decompressed = decompress(parsedInput.image, parsedInput.algorithm, 50)
    return decompressed.arr.sumOf { row -> row.sumOf { it } }
}

fun parseInput(inputFile: File): ParsedInput {
    val lines = inputFile.readLines()
    val algo = lines.first().map { if (it == '.') 0 else 1 }
    Logger.debug(algo)
    val image = lines
        .drop(2)
        .filter(String::isNotBlank)
        .map { line -> Array(line.length) { if (line[it] == '.') 0 else 1 } }
        .toTypedArray()
        .let(::Grid)
    Logger.debug("\n{}", image.asString())
    return ParsedInput(image, algo)
}

fun main() {
    val inputFile = File("days/src/main/resources/Day20.txt")
    val parsedInput = parseInput(inputFile)
    timeit("Part 1:") { part1(parsedInput) }
    timeit("Part 2:") { part2(parsedInput) }
}

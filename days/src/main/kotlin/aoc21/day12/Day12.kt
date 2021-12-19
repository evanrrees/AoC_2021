package aoc21.day12

import utils.split.mapSplit
import java.io.File

class Day12(inputFile: File, private val maxVisits: Int = 1) {
    private val caves = inputFile.useLines { lines ->
        lines.mapSplit("-") { (a, b) -> listOf(a to b, b to a) }.flatten()
            .groupBy({ it.first }, { it.second })
    }
    var counter = 0
    private val path = mutableListOf<String>()
    private val cts = Array(caves.size) { 0 }
    private val canVisitTwice: Boolean get() = caves.keys.zip(cts) { k, v -> k.isBig() || v < maxVisits }.all()

    init {
        findPaths()
    }

    private fun String.isBig() = first().isUpperCase()

    private fun findPaths(cave: String = "start") {
        if (cave == "end") counter++
        else if (cave.isBig() || cave !in path || canVisitTwice) {
            path.add(cave)
            cts[caves.keys.indexOf(cave)]++
            caves[cave]!!.filter { it != "start" }.forEach(::findPaths)
            path.removeLast()
            cts[caves.keys.indexOf(cave)]--
        }
    }
}

fun Iterable<Boolean>.all(): Boolean = all { it }
fun Sequence<Boolean>.all(): Boolean = all { it }

fun part1(inputFile: File) = Day12(inputFile).counter

fun part2(inputFile: File) = Day12(inputFile, 2).counter

fun main() {
    val inputFile = File("src/main/resources/Day12.txt")
    part1(inputFile).let(::println)
    part2(inputFile).let(::println)
}

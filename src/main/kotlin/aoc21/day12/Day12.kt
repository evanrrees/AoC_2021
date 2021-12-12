package aoc21.day12

import aoc21.utils.split.forEachSplit
import java.io.File


fun findPaths(
    caves: Map<String, List<String>>,
    paths: MutableList<MutableList<String>>,
    maxVisits: Int = 1,
    cave: String = "start",
    counts: MutableMap<String, Int> = mutableMapOf(),
    path: MutableList<String> = mutableListOf()
) {
    if (cave == "end") paths.add((path + "end").toMutableList())
    else if (
        cave.first().isUpperCase() || cave !in path ||
        counts.all { (k, v) -> k.first().isUpperCase() || v < maxVisits }
    ) {
        path.add(cave)
        counts.compute(cave) { _, v -> v?.let { it + 1 } ?: 1 }
        for (c in caves[cave]!!) if (c != "start") findPaths(caves, paths, maxVisits, c, counts, path)
        path.removeLast()
        counts.compute(cave) { _, v -> v?.let { it - 1 }?.takeIf { it > 0 } }
    }
}

fun part1(caveSystem: Map<String, List<String>>) = mutableListOf<MutableList<String>>().also { findPaths(caveSystem, it) }.size

fun part2(caveSystem: Map<String, List<String>>) = mutableListOf<MutableList<String>>().also { findPaths(caveSystem, it, 2) }.size

fun parseInput(inputFile: File): Map<String, List<String>> {
    val caves = mutableMapOf<String, MutableList<String>>()
    inputFile.useLines { lines ->
        lines.forEachSplit("-") { (a, b) ->
            caves[a]?.add(b) ?: caves.put(a, mutableListOf(b))
            caves[b]?.add(a) ?: caves.put(b, mutableListOf(a))
        }
    }
    return caves
}

fun main() {
    val inputFile = File("src/main/resources/Day12.txt")
    val parsedInput = parseInput(inputFile)
    part1(parsedInput).let(::println)
    part2(parsedInput).let(::println)
}

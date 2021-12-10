package aoc21.day09

import aoc21.utils.konsole.*
import java.io.File

typealias Heightmap = Array<Array<Int>>
operator fun Heightmap.get(point: Point) = this[point.i][point.j]
operator fun Heightmap.set(point: Point, value: Int) = this[point.i].set(point.j, value)

fun printState(basinMap: Heightmap) {
    val stamp = System.currentTimeMillis()
    File("frames/heights_$stamp.csv").bufferedWriter().use { writer ->
        basinMap.forEach {
            writer.appendLine(it.joinToString(","))
        }
    }
    Thread.sleep(1)
}

data class Point(val i: Int, val j: Int, val map: Heightmap) {
    val value get() = map[i][j]

    override fun toString() = "($i,$j)"

    fun isLowpoint() = adjacent().all { it.value > value }

    fun expand(basinMap: Heightmap, basinID: Int) {
        File("frames/path.txt").appendText("$i,$j\n")
        basinMap[this] = basinID
        adjacent()
            .filter { it.value > value }
            .filter { basinMap[it] == 0 }
            .filter { it.value != 9 }
            .forEach {
                it.expand(basinMap, basinID)
            }
    }

    fun adjacent(): List<Point> {
        val points = mutableListOf<Point>()
        if (i > 0)                points += Point(i - 1, j, map)
        if (i < map.lastIndex)    points += Point(i + 1, j, map)
        if (j > 0)                points += Point(i, j - 1, map)
        if (j < map[i].lastIndex) points += Point(i, j + 1, map)
        return points
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (i != other.i) return false
        if (j != other.j) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = i
        result = 31 * result + j
        result = 31 * result + value
        return result
    }

}

fun Heightmap.format() = indices.joinToString("\n", prefix = "Heightmap:\n", postfix = "\n") { i ->
    this[i].indices.joinToString("") { j ->
        val point = Point(i, j, this)
        "${point.value}".let { if (point.isLowpoint()) it.format { white; bright }.reset() else it }
    }
}

fun Heightmap.format(
    basins: Heightmap,
    palette: List<Color> =
        listOf(Color.WHITE, Color.CYAN, Color.PURPLE, Color.BLUE, Color.YELLOW, Color.GREEN, Color.RED)
) =
    indices.joinToString("\n", prefix = "Heightmap:\n", postfix = "\n") { i ->
        this[i].indices.joinToString(" ") { j ->
            val point = Point(i, j, this)
            "${point.value}".format {
                if (point.value == 9) { black; intensity = Intensity.STANDARD }
                else {
                    color = palette[basins[point] % palette.size]
                    if (point.isLowpoint()) { bold; bright }
                }
            }
                .reset()
        }
    }

fun part1(heightmap: Heightmap): Int {
    val lowpoints = Array(heightmap.size) { i -> Array(heightmap[i].size) { 0 } }
    val risk = heightmap.indices.sumOf { i ->
        heightmap[i].indices.sumOf { j ->
            val point = Point(i, j, heightmap)
            if (point.isLowpoint()) {
                lowpoints[point] = 1
                point.value + 1
            } else {
                0
            }
        }
    }
    println(heightmap.format(lowpoints))
    return risk
}

fun part2(heightmap: Heightmap): Int {
    val basins = Array(heightmap.size) { Array(heightmap[it].size) { 0 } }
    var basinID = 0
    for (i in basins.indices) {
        for (j in basins[i].indices) {
            val p = Point(i, j, heightmap)
            if (basins[p] != 0) continue
            val adj = p.adjacent()
                .filter { basins[it] == 0 }
                .filter { it.value != 9 }
            if (adj.isNotEmpty() && adj.all { it.value >= p.value }) {
                basins[p] = ++basinID
//                printState(basins)
                File("frames/path.txt").appendText("$i,$j\n")
                adj.forEach { it.expand(basins, basinID) }
            }
        }
    }

    println(heightmap.format(basins))

    return basins.flatten()
        .filter { it != 0 }
        .groupingBy { it }.eachCount()
        .values
        .sortedDescending()
        .take(3).reduce(Int::times)

}


fun parseInput(inputFile: File) = inputFile.useLines { lines ->
    lines.map { it.map(Char::digitToInt).toTypedArray() }.toList()
}.toTypedArray()

fun main() {
    val inputFile = File("src/main/resources/Day09.txt")
    val parsedInput = parseInput(inputFile)
    val result1 = part1(parsedInput)
    println(result1)
    val result2 = part2(parsedInput)
    println(result2)
}

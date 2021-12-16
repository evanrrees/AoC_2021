package aoc21.day15

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File
import kotlin.math.exp

internal class Day15KtTest {

    val inputFile = File("src/test/resources/Day15.txt")
    val parsedInput = parseInput(inputFile)

    @Test
    fun part1() {
        var expect = 40
        var actual = part1(parsedInput)
        assertEquals(expect, actual)
        expect = 315
        actual = part1(parsedInput.expand())
        assertEquals(expect, actual)
    }

    @Test
    fun expandGrid() {
        val expect = parseInput(File("src/test/resources/day15/expanded_grid.txt"))
        val actual = parseInput(inputFile).expand()
        assertEquals(expect, actual)
    }

    @Test
    fun part2() {
        var expect = 40
        var actual = part2(parsedInput)
        assertEquals(expect, actual)
        expect = 315
        actual = part2(parsedInput.expand())
        assertEquals(expect, actual)
    }

//    @Test
//    fun foo() {
//        val start = Point(0, 0)
//        val end = Point(arr.lastIndex, arr.last().lastIndex)
//        val graph2 = RiskGraph2(arr)
//        val path2 = graph2.shortestPath(start, end)
//        println(path2)
//        println(path2.sumOf { arr[it] } - arr[start])
//    }

//    @Test
//    fun dpoint() {
//        val arr2 = expandGrid(arr)
//        val arr3 = parsedInput.expand()
//        val graph = RiskGraph(arr3)
//        val expect = 315
//        val actual = graph.costOfShortestPath()
//        assertEquals(expect, actual)
//    }

}

fun expandGrid(arr: Grid<Int>): Grid<Int> {
    val height = arr.size
    val width = arr.map { it.size }.distinct().single()
    val result = Array(height * 5) { Array(width * 5) { 0 } }
    for (i in result.indices) {
        for (j in result[i].indices) {
            result[i][j] =
                when {
                    i < height    -> if (j < width) arr[i][j] else result[i][j - width] + 1
                    j < width * 4 -> result[i - height][j + width]
                    else          -> result[i][j - width] + 1
                }
                    .let { if (it > 9) it - 9 else it }
        }
    }
    return result
}

val arr = arrayOf(
    arrayOf(1, 1, 6, 3, 7, 5, 1, 7, 4, 2),
    arrayOf(1, 3, 8, 1, 3, 7, 3, 6, 7, 2),
    arrayOf(2, 1, 3, 6, 5, 1, 1, 3, 2, 8),
    arrayOf(3, 6, 9, 4, 9, 3, 1, 5, 6, 9),
    arrayOf(7, 4, 6, 3, 4, 1, 7, 1, 1, 1),
    arrayOf(1, 3, 1, 9, 1, 2, 8, 1, 3, 7),
    arrayOf(1, 3, 5, 9, 9, 1, 2, 4, 2, 1),
    arrayOf(3, 1, 2, 5, 4, 2, 1, 6, 3, 9),
    arrayOf(1, 2, 9, 3, 1, 3, 8, 5, 2, 1),
    arrayOf(2, 3, 1, 1, 9, 4, 4, 5, 8, 1)
)

open class Point(val i: Int, val j: Int) {
    open val adjacent: List<Point> get() = listOf(Point(i - 1, j), Point(i + 1, j), Point(i, j - 1), Point(i, j + 1))
}

typealias Grid<T> = Array<Array<T>>
operator fun <T> Grid<T>.get(point: Point) = this[point.i][point.j]
operator fun <T> Grid<T>.set(point: Point, value: T) {
    this[point.i][point.j] = value
}
operator fun <T> Grid<T>.contains(point: Point) = point.i in indices && point.j in this[point.i].indices

fun <T> Grid<T>.firstPoint() = Point(0, 0)
fun <T> Grid<T>.lastPoint() = Point(lastIndex, last().lastIndex)

fun <T> Grid<T>.pointSequence(): Sequence<Point> = sequence {
    indices.forEach { i -> get(i).indices.forEach { j -> yield(Point(i, j)) } }
}
fun <T> Grid<T>.points() = indices.flatMap { i -> this[i].indices.map { j -> Point(i, j) } }
fun <T> Grid<T>.elements() = flatten()
inline fun <reified T> Grid<T>.deepCopyOf() = Array(size) { this[it].copyOf() }
inline fun <reified T> createGrid(nrows: Int, ncols: Int, init: (Int, Int) -> T): Grid<T> =
    Array(nrows) { i -> Array(ncols) { init(i, it) } }
inline fun <reified T> createGrid(nrows: Int, ncols: Int, init: T): Grid<T> = Array(nrows) { Array(ncols) { init } }
inline fun <reified T> createGrid(size: Int, init: (Int, Int) -> T): Grid<T> =
    Array(size) { i -> Array(size) { j -> init(i, j) } }
inline fun <reified T> createGrid(size: Int, init: T): Grid<T> = Array(size) { Array(size) { init } }

class RiskGraph2(
    val risks: Grid<Int>
) {

    fun dijkstra(start: Point): Grid<Point?> {
        val known = createGrid(risks.size, risks.last().size, false)
        val delta = createGrid(risks.size, risks.last().size, Int.MAX_VALUE)
        delta[start] = 0
        val previous: Grid<Point?> = createGrid(risks.size, risks.last().size, null)
        repeat(risks.sumOf { it.size }) {
            val v = delta
                .points()
                .filter { !known[it] }
                .minByOrNull { delta[it] }!!
            v.adjacent
                .filter { it in risks }
                .filter { !known[it] }
                .forEach { u ->
                    val alt = delta[v] + risks[u]
                    if (alt < delta[u]) {
                        delta[u] = alt
                        previous[u] = v
                    }
                }
            known[v] = true
        }
        return previous.deepCopyOf()
    }

    fun shortestPath(start: Point, end: Point): List<Point> {
        val shortestPathTree = dijkstra(start)
        fun pathTo(start: Point, end: Point): List<Point> =
            if (shortestPathTree[end] == null) listOf(end)
            else listOf(pathTo(start, shortestPathTree[end]!!), listOf(end)).flatten()
        return pathTo(start, end)
    }

}

fun p2(risks: Grid<Int>): Int {
    val grid = createGrid(risks.size, risks.size) { i, j -> RiskGraph.DPoint(i, j, risks[i][j]) }
    grid.first().first().delta = 0
    repeat(grid.sumOf { it.size }) {
        val v = grid.flatten()
            .filterNot { it.known }
            .minByOrNull { it.delta }!!
            .also { it.known = true }
        v.neighborsIn(grid)
            .filterNot { it.known }
            .forEach { u ->
                val alt = v.delta + u.risk
                if (alt < u.delta) {
                    u.delta = alt
                    u.previous = v
                }
            }
    }

    var curr = grid.last().last().previous!!
    var cost = 0
    while (curr != grid.first().first()) {
        cost += curr.risk
        curr = curr.previous!!
    }

    return cost
}

class RiskGraph(risks: Grid<Int>) {

    val grid = createGrid(risks.size, risks.size) { i, j -> DPoint(i, j, risks[i][j]) }

    fun calc(): Int {
        val path = mutableListOf<Point>()
        var curr: DPoint? = grid.last().last()
        var cost = 0
        while (curr != grid.first().first()) {
            cost += curr!!.risk
            curr = grid[curr]
        }
        return cost
    }

    fun dijkstra(start: Point): Grid<Point?> {
        grid[start].delta = 0
        repeat(grid.sumOf { it.size }) {
            val v = grid
                .flatten()
                .filterNot { it.known }
                .minByOrNull { it.delta }!!
            v.neighborsIn(grid)
                .filterNot { it.known }
                .forEach { u ->
                    val alt = v.delta + u.risk
                    if (alt < u.delta) {
                        u.delta = alt
                        u.previous = v
                    }
                }
            v.known = true
        }
        return grid.map { row -> row.map { it.previous as Point? }.toTypedArray() }.toTypedArray()
    }

    fun shortestPath(start: Point, end: Point): List<Point> {
        val traceback = dijkstra(start)
        val path = mutableListOf<Point>()
        var curr: Point? = end
        while (curr != null) {
            path.add(0, curr)
            curr = traceback[curr]
        }
        return path

//        tailrec fun path2(start: Point, end: Point) {
//            if (traceback[end] == null) return
//            else return path2(start, traceback[end]!!)
//        }
//
//        fun pathTo(start: Point, end: Point): List<Point> {
//            return if (traceback[end] == null) listOf(end)
//            else pathTo(start, traceback[end]!!) + listOf(end)
//        }
//        return pathTo(start, end)
    }

    class DPoint(
        i: Int,
        j: Int,
        val risk: Int,
        var known: Boolean = false,
        var delta: Int = Int.MAX_VALUE,
        var previous: DPoint? = null
    ): Point(i, j) {
        fun neighborsIn(grid: Grid<DPoint>): List<DPoint> {
            val list = mutableListOf<DPoint>()
            if (i > 0)                 list += grid[i - 1][j]
            if (i < grid.lastIndex)    list += grid[i + 1][j]
            if (j > 0)                 list += grid[i][j - 1]
            if (j < grid[i].lastIndex) list += grid[i][j + 1]
            return list
        }

        override fun toString(): String {
            return "DPoint(i=$i, j=$j, risk=$risk, known=$known, delta=$delta, previous=${previous.hashCode()}"
        }
    }

    fun costOfShortestPath(): Int {
        val path = shortestPath(grid.first().first(), grid.last().last())
        return path.sumOf { grid[it].risk } - grid.first().first().risk
    }

}
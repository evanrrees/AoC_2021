package aoc21.utils

import aoc21.utils.ranges.progressionTo

data class Point(val x: Int, val y: Int) {
    constructor(pair: Pair<Int, Int>): this(pair.first, pair.second)
    constructor(list: List<Int>): this(list[0], list[1])
    constructor(vararg ints: Int): this(ints[0], ints[1])
}

data class Line(val start: Point, val end: Point) {
    val isStraight = (start.y == end.y) xor (start.x == end.x)
    val xRange     = start.x progressionTo end.x
    val yRange     = start.y progressionTo end.y
    val xMax       = maxOf(start.x, end.x)
    val yMax       = maxOf(start.y, end.y)

    constructor(Points: List<Point>): this(Points[0], Points[1])

    fun points() = if (isStraight) yRange.zip(xRange, ::Point) else yRange.expand(xRange, ::Point)
    fun drawTo(array: Array<Array<Int>>) = points().forEach { array[it.y][it.x]++ }
}
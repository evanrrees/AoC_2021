package aoc21.utils.geom

import aoc21.utils.grids.Point
import aoc21.utils.ranges.expand
import aoc21.utils.ranges.progressionTo

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
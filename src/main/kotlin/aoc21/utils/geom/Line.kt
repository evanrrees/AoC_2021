package aoc21.utils.geom

import aoc21.utils.grids.Grid
import aoc21.utils.grids.Point
import aoc21.utils.ranges.expand

class Line(x0: Int, x1: Int, y0: Int, y1: Int): Rectangle(x0, x1, y0, y1) {
    constructor(start: Point, end: Point): this(start.x, end.x, start.y, end.y)
    constructor(Points: List<Point>):      this(Points[0], Points[1])
    val isStraight = (y0 == y1) xor (x0 == x1)
    fun points() = if (isStraight) yRange.zip(xRange, ::Point) else yRange.expand(xRange, ::Point)
    fun drawTo(grid: Grid<Int>) = points().forEach { grid[it.y, it.x]++ }
}
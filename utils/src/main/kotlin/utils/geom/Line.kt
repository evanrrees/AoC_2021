package utils.geom

import utils.grids.Grid
import utils.grids.Point
import utils.ranges.expand

class Line(x0: Int, x1: Int, y0: Int, y1: Int): Rectangle(x0, x1, y0, y1) {
    constructor(start: Point, end: Point): this(start.x, end.x, start.y, end.y)
    constructor(Points: List<Point>):      this(Points[0], Points[1])
    val isStraight = (y0 == y1) xor (x0 == x1)
    fun points() = yRange.zip(xRange, ::Point)
    fun drawTo(grid: Grid<Int>) = points().forEach { grid[it.y, it.x]++ }
}
package aoc21.utils.geom

import aoc21.utils.grids.Point

class Rectangle(x0: Int, x1: Int, y0: Int, y1: Int) {
    constructor(vararg ints: Int): this(ints[0], ints[1], ints[2], ints[3])
    val xRange = minOf(x0, x1) .. maxOf(x0, x1)
    val yRange = minOf(y0, y1) .. maxOf(y0, y1)
    operator fun contains(point: Point) = point.y in yRange && point.x in xRange
}
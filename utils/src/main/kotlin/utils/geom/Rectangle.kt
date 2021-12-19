package utils.geom

import utils.grids.Point


open class Rectangle(x0: Int, x1: Int, y0: Int, y1: Int) {
    constructor(vararg ints: Int): this(ints[0], ints[1], ints[2], ints[3])
    open val xRange = minOf(x0, x1) .. maxOf(x0, x1)
    open val yRange = minOf(y0, y1) .. maxOf(y0, y1)
    val xMax get() = xRange.last
    val yMax get() = yRange.last
    val xMin get() = xRange.first
    val yMin get() = yRange.first
    operator fun contains(point: Point) = point.y in yRange && point.x in xRange
}

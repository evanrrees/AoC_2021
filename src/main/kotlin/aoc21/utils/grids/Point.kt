package aoc21.utils.grids


open class Point(val i: Int, val j: Int) {

    constructor(pair: Pair<Int, Int>): this(pair.first, pair.second)

    fun adjacent() = listOf(
        Point(i - 1, j),
        Point(i + 1, j),
        Point(i, j - 1),
        Point(i, j + 1)
    )

    fun adjacent(diagonal: Boolean = false) =
        if (diagonal) adjacent()
        else {
            val list = mutableListOf<Point>()
            for (m in -1 .. 1)
                for (n in -1 .. 1)
                    if (m != 0 && n != 0) list += Point(m, n)
            list
        }

    fun <T> adjacentValuesIn(grid: Grid<T>): List<T> {
        val list = mutableListOf<T>()
        if (i > 0)                  list += grid[i - 1, j]
        if (i < grid.lastRowIndex)     list += grid[i + 1, j]
        if (j > 0)                  list += grid[i, j - 1]
        if (j < grid.lastColIndex)  list += grid[i, j + 1]
        return list
    }

    fun <T> adjacentPointsIn(grid: Grid<T>): List<Point> {
        val list = mutableListOf<Point>()
        if (i > 0)                  list += Point(i - 1, j)
        if (i < grid.lastRowIndex)  list += Point(i + 1, j)
        if (j > 0)                  list += Point(i, j - 1)
        if (j < grid.lastColIndex)  list += Point(i, j + 1)
        return list
    }

    fun <T> adjacentPointSequenceIn(grid: Grid<T>) = sequence {
        if (i > 0)                  yield(Point(i - 1, j))
        if (i < grid.lastRowIndex)  yield(Point(i + 1, j))
        if (j > 0)                  yield(Point(i, j - 1))
        if (j < grid.lastColIndex)  yield(Point(i, j + 1))
    }

    fun <T> forEachAdjacentIn(grid: Grid<T>, action: (T) -> Unit) {
        if (i > 0)                  action(grid[i - 1, j])
        if (i < grid.lastRowIndex)  action(grid[i + 1, j])
        if (j > 0)                  action(grid[i, j - 1])
        if (j < grid.lastColIndex)  action(grid[i, j + 1])
    }

    operator fun plus(other: Point)  = Point(i + other.i, j + other.j)
    operator fun minus(other: Point) = Point(i - other.i, j - other.j)
    operator fun times(other: Point) = Point(i * other.i, j * other.j)
    operator fun div(other: Point)   = Point(i / other.i, j / other.j)

    fun pointAbove() = Point(i - 1, j)
    fun pointBelow() = Point(i + 1, j)
    fun pointLeft()  = Point(i, j - 1)
    fun pointRight() = Point(i, j + 1)

    infix fun isAbove(other: Point)   = i < other.i
    infix fun isBelow(other: Point)   = i > other.i
    infix fun isLeftOf(other: Point)  = j < other.j
    infix fun isRightOf(other: Point) = j > other.j

    operator fun compareTo(other: Point): Int {
        val vert  = i - other.i
        val horiz = j - other.j
        return if (vert < horiz) vert else horiz
    }

    operator fun component1() = i
    operator fun component2() = j

}

fun Pair<Int, Int>.toPoint() = Point(this)
package utils.grids

//fun Array<Array<Boolean>>.all() = all { row -> row.all { it } }

//operator fun <T> Array<Array<T>>.get(i: Int, j: Int) = this[i][j]
//operator fun <T> Array<Array<T>>.set(i: Int, j: Int, value: T) { this[i][j] = value }

inline fun <reified T> Array<Array<T>>.deepCopyOf() = Array(size) { this[it].copyOf() }

//fun <T> Array<Array<T>>.forEachElement(action: (T) -> Unit) = forEach { arr -> arr.forEach { action(it) } }
//fun <T> Array<Array<T>>.forEachElementIndexed(action: (i: Int, j: Int, it: T) -> Unit) =
//    forEachIndexed { i, arr -> arr.forEachIndexed { j, x -> action(i, j, x) } }
//
//fun <T, R> Array<Array<T>>.mapElements(action: (T) -> R) = map { arr -> arr.map { action(it) } }
//fun <T, R> Array<Array<T>>.mapElementsIndexed(action: (i: Int, j: Int, it: T) -> R) =
//    mapIndexed { i, arr -> arr.mapIndexed { j, it -> action(i, j, it) } }
//
//fun <T> Array<Array<T>>.gridToString(
//    delimiter: String = ", ",
//    lineSeparator: String = System.lineSeparator(),
//    header: String? = null,
//    rowLimit: Int = -1,
//    colLimit: Int = -1,
//    transform: ((T) -> CharSequence)? = null,
//) =
//    joinToString(
//        separator = lineSeparator,
//        prefix = header?.let { "$it$lineSeparator" } ?: "",
//        postfix = lineSeparator,
//        limit = rowLimit
//    ) { row ->
//        row.run { joinToString(separator = delimiter, limit = colLimit, transform = transform) }
//    }

//operator fun <T> Grid<T>.get(pair: Pair<Int, Int>): T = this[pair.first][pair.second]

//open class Point(val i: Int, val j: Int) {
//    open val adjacent: List<Point> get() = listOf(Point(i - 1, j), Point(i + 1, j), Point(i, j - 1), Point(i, j + 1))
//}

//typealias Grid<T> = Array<Array<T>>

//operator fun <T> Grid<T>.get(point: Point) = this[point.i][point.j]
//operator fun <T> Grid<T>.set(point: Point, value: T) {
//    this[point.i][point.j] = value
//}
//operator fun <T> Grid<T>.contains(point: Point) = point.i in indices && point.j in this[point.i].indices
//inline fun <reified T> createGrid(nrows: Int, ncols: Int, init: (Int, Int) -> T): Grid<T> =
//    Array(nrows) { i -> Array(ncols) { init(i, it) } }
//inline fun <reified T> createGrid(nrows: Int, ncols: Int, init: T): Grid<T> = Array(nrows) { Array(ncols) { init } }
//inline fun <reified T> createGrid(size: Int, init: (Int, Int) -> T): Grid<T> =
//    Array(size) { i -> Array(size) { j -> init(i, j) } }
//inline fun <reified T> createGrid(size: Int, init: T): Grid<T> = Array(size) { Array(size) { init } }
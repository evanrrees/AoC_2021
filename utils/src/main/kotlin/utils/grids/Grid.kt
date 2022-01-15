package utils.grids

inline fun <reified T> gridOf(rows: Int, cols: Int, init: (i: Int, j: Int) -> T): Grid<T> {
    return Grid(Array(rows) { i -> Array(cols) { j -> init(i, j) } })
}

open class Grid<T>(val arr: Array<Array<T>>, val rows: Int = arr.size, val cols: Int = arr.distinctBy { it.size }.single().size) {

    val size get() = rows * cols
    val lastRowIndex get() = rows - 1
    val lastColIndex get() = cols - 1
    fun first() = this[0, 0]
    fun last() = this[rows - 1, cols - 1]
    val rowIndices = 0..lastRowIndex
    val colIndices = 0..lastColIndex

    fun row(index: Int) = arr[index]

    companion object {
        inline operator fun <reified T> invoke() = Grid(Array(0) { emptyArray<T>() })
        inline operator fun <reified T> invoke(rows: Int, cols: Int): Grid<T> {
            val def = when (T::class){
                 Boolean::class -> false
                    Byte::class -> (0).toByte()
                   Short::class -> (0).toShort()
                     Int::class -> 0
                    Long::class -> 0L
                   UByte::class -> (0).toUByte()
                  UShort::class -> (0).toUShort()
                    UInt::class -> 0U
                   ULong::class -> 0UL
                   Float::class -> 0f
                  Double::class -> 0f.toDouble()
                  String::class -> ""
                    Char::class -> Char.MIN_VALUE
                else -> null
            }
            return Grid(Array(rows) { Array(cols) { def as T } }, rows, cols)
        }
        inline operator fun <reified T> invoke(rows: Int, cols: Int, init: (i: Int, j: Int) -> T) =
            Grid(Array(rows) { i -> Array(cols) { j -> init(i, j) } }, rows, cols)
        inline operator fun <reified T> invoke(rows: Int, cols: Int, default: T) = Grid(rows, cols) { _, _ -> default }
        inline operator fun <reified T> invoke(source: Collection<Collection<T>>) =
            Grid(Array(source.size) { source.elementAt(it).toTypedArray() })
    }

    open operator fun get(i: Int, j: Int) = arr[i][j]
    operator fun get(i: Int) = arr[i]
//    operator fun get(j: Int, i: IntRange = 0..lastRowIndex) = arr.su
    operator fun get(point: Point) = arr[point.i][point.j]

    operator fun set(i: Int, j: Int, value: T) { arr[i][j] = value }
    operator fun set(point: Point, value: T) { arr[point.i][point.j] = value }

    operator fun contains(element: T) = arr.any { element in it }

    operator fun contains(other: Grid<T>): Boolean = TODO()

    override fun equals(other: Any?): Boolean {
        if (other as? Grid<*> != null) {
            if (size != other.size) return false
            if (rows != other.rows) return false
            if (cols != other.cols) return false
            if (!arr.contentDeepEquals(other.arr)) return false
            return true
        }
        return super.equals(other)
    }

    override fun hashCode() = arr.contentDeepHashCode()

    fun points() = arr.indices.flatMap { i -> arr[i].indices.map { j -> Point(i, j) } }
    fun pointSequence() = sequence { arr.indices.forEach { i -> arr[i].indices.forEach { j -> yield(Point(i, j)) } } }

    override fun toString(): String {
        val width = maxOf { "$it".length }
        val prefix = "${this::class.simpleName}(rows=$rows, cols=$cols)\n"
        return arr.joinToString("\n", prefix) { row -> row.joinToString(" ") { "$it".padStart(width) } }
    }

    fun toCSV() = arr.joinToString("\n") { it.joinToString("\n") }

}

fun <T> Grid<T>.toString(colsep: String) = arr.joinToString("\n") { it.joinToString(colsep) }

fun <T> Grid<T>.all(predicate: (it: T) -> Boolean): Boolean {
    for (a in arr) for (it in a) if (!predicate(it)) return false
    return true
}

//inline fun <reified G : Grid<T>, T, reified R> G.map(transform: (it: T) -> R): G {
//    return Grid(rows, cols) { i, j -> transform(arr[i][j]) }
//}

inline fun <T, R : Comparable<R>> Grid<T>.maxOf(selector: (T) -> R) = arr.maxOf { row -> row.maxOf { selector(it) } }

fun <T> Grid<T>.forEachIndexed(action: (i: Int, j: Int, it: T) -> Unit) =
    arr.forEachIndexed { i, p -> p.forEachIndexed { j, t -> action(i, j, t) } }

fun <T> Grid<T>.forEach(action: (it: T) -> Unit) = arr.forEach { it.forEach(action) }

inline fun <T, reified R> Grid<T>.mapIndexed(transform: (i: Int, j: Int, it: T) -> R) =
    Grid(rows, cols) { i, j -> transform(i, j, arr[i][j]) }

fun <T> Grid<T>.getOrElse(i: Int, j: Int, defaultValue: (Int, Int) -> T): T {
    return if (i in 0..lastRowIndex) arr[i][j] else defaultValue(i, j)
}

fun <T> Grid<T>.getOrElse(i: Int, defaultValue: (Int) -> Array<T>): Array<T> {
    return if (i in 0..lastRowIndex) arr[i] else defaultValue(i)
}

fun <T> Grid<T>.getOrDefault(i: Int, j: Int, defaultValue: T): T {
    return if (i in 0..lastRowIndex) arr[i][j] else defaultValue
}

fun <T> Grid<T>.getOrDefault(i: Int, defaultValue: Array<T>): Array<T> {
    return if (i in 0..lastRowIndex) arr[i] else defaultValue
}

fun <T> Grid<T>.getOrNull(i: Int, j: Int): T? {
    return if (i in 0..lastRowIndex && j in 0..lastColIndex) arr[i][j] else null
}

fun <T> Grid<T>.getOrNull(i: Int): Array<T>? {
    return if (i in 0..lastRowIndex) arr[i] else null
}

inline fun <reified T> Grid<T>.copyOf(): Grid<T> = Grid(Array(rows) { arr[it].copyOf() })

inline fun <reified T> Collection<Collection<T>>.asGrid(): Grid<T> {
    val rows = this.size
    val cols = this.distinctBy { it.size }.single().size
    return Grid(rows, cols) { i, j -> this.elementAt(i).elementAt(j) }
}

inline fun <reified T, R, reified V> Collection<T>.expandToGrid(other: Collection<R>, transform: (a: T, b: R) -> V) =
    Grid(size, other.size) { i, j -> transform(this.elementAt(i), other.elementAt(j)) }

inline fun <reified T, R, reified V> Array<T>.expandToGrid(other: Array<R>, transform: (a: T, b: R) -> V) =
    Grid(size, other.size) { i, j -> transform(this.elementAt(i), other.elementAt(j)) }
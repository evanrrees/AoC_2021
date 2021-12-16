package aoc21.utils.grids


open class Grid<T>(val arr: Array<Array<T>>, val rows: Int = arr.size, val cols: Int = arr.distinctBy { it.size }.single().size) {

    val size get() = rows * cols
    val lastRowIndex get() = rows - 1
    val lastColIndex get() = cols - 1
    fun first() = this[0, 0]
    fun last() = this[rows - 1, cols - 1]

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
        inline operator fun <reified T> invoke(rows: Int, cols: Int, init: (Int, Int) -> T) =
            Grid(Array(rows) { i -> Array(cols) { j -> init(i, j) } }, rows, cols)
        inline operator fun <reified T> invoke(rows: Int, cols: Int, default: T) = Grid(rows, cols) { _, _ -> default }
    }

    operator fun get(i: Int, j: Int) = arr[i][j]
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

    fun forEach(action: (T) -> Unit) = arr.forEach { it.forEach(action) }
    fun forEachIndexed(action: (i: Int, j: Int, it: T) -> Unit) =
        arr.forEachIndexed { i, p -> p.forEachIndexed { j, t -> action(i, j, t) } }

    fun points() = arr.indices.flatMap { i -> arr[i].indices.map { j -> Point(i, j) } }
    fun pointSequence() = sequence { arr.indices.forEach { i -> arr[i].indices.forEach { j -> yield(Point(i, j)) } } }

    inline fun <reified R> map(transform: (T) -> R) = Grid(rows, cols) { i, j -> transform(arr[i][j]) }

    inline fun <R : Comparable<R>> maxOf(selector: (T) -> R) = arr.maxOf { row -> row.maxOf { selector(it) } }

    override fun toString(): String {
        val width = maxOf { "$it".length }
        val prefix = "${this::class.simpleName}(rows=$rows, cols=$cols)\n"
        return arr.joinToString("\n", prefix) { row ->
            row.joinToString(" ") { "$it".padStart(width) }
        }
    }

}

inline fun <reified T> Collection<Collection<T>>.asGrid(): Grid<T> {
    val rows = this.size
    val cols = this.distinctBy { it.size }.single().size
    return Grid(rows, cols) { i, j -> this.elementAt(i).elementAt(j) }
}

//class Grid3<T>(rows: Int, cols: Int, init: (Int, Int) -> T): Grid2<T>(rows, cols, init) {
////    val arr1 = Array(cols) { init(0, it) }
////    override val arr: Array<Array<T>> = Array<Array<T>>(rows) { i -> Array<T>(cols) { j -> init(i, j)} }
//}
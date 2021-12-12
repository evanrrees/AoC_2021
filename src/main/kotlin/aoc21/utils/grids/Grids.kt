package aoc21.utils.grids

fun Array<Array<Boolean>>.all() = all { row -> row.all { it } }

operator fun <T> Array<Array<T>>.get(i: Int, j: Int) = this[i][j]
operator fun <T> Array<Array<T>>.set(i: Int, j: Int, value: T) { this[i][j] = value }

inline fun <reified T> Array<Array<T>>.deepCopyOf() = Array(size) { this[it].copyOf() }
val <T> Array<Array<T>>.deepSize get() = sumOf { it.size }

val <T> Array<Array<T>>.lastRowIndex get() = lastIndex
// assumes square grid
val <T> Array<Array<T>>.lastColIndex get() = first().lastIndex

fun <T> Array<Array<T>>.setAll(value: T) = forEachElementIndexed { i, j, _ -> this[i][j] = value }

fun <T> Array<Array<T>>.forEachElement(action: (T) -> Unit) = forEach { arr -> arr.forEach { action(it) } }
fun <T> Array<Array<T>>.forEachElementIndexed(action: (i: Int, j: Int, it: T) -> Unit) =
    forEachIndexed { i, arr -> arr.forEachIndexed { j, x -> action(i, j, x) } }

fun <T, R> Array<Array<T>>.mapElements(action: (T) -> R) = map { arr -> arr.map { action(it) } }
fun <T, R> Array<Array<T>>.mapElementsIndexed(action: (i: Int, j: Int, it: T) -> R) =
    mapIndexed { i, arr -> arr.mapIndexed { j, it -> action(i, j, it) } }

fun <T> Array<Array<T>>.gridToString(
    delimiter: String = ", ",
    lineSeparator: String = System.lineSeparator(),
    header: String? = null,
    rowLimit: Int = -1,
    colLimit: Int = -1,
    transform: ((T) -> CharSequence)? = null,
) =
    joinToString(
        separator = lineSeparator,
        prefix = header?.let { "$it$lineSeparator" } ?: "",
        postfix = lineSeparator,
        limit = rowLimit
    ) { row ->
        row.run { joinToString(separator = delimiter, limit = colLimit, transform = transform) }
    }
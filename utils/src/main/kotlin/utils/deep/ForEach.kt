package utils.deep

fun <T> Iterable<Iterable<T>>.forEachDeep(operation: (it: T) -> Unit) {
    for (outer in this) for (inner in outer)
        operation(inner)
}

fun <T> Iterable<Iterable<T>>.forEachDeep(operation: (outer: Iterable<T>, inner: T) -> Unit) {
    for (outer in this) for (inner in outer)
        operation(outer, inner)
}

fun <T> Iterable<Iterable<T>>.forEachDeepIndexed(operation: (i: Int, outer: Iterable<T>, j: Int, inner: T) -> Unit) {
    for ((i, outer) in this.withIndex()) for ((j, inner) in outer.withIndex())
        operation(i, outer, j, inner)
}

fun <T> Array<Array<out T>>.forEachDeep(operation: (it: T) -> Unit) {
    for (outer in this) for (inner in outer)
        operation(inner)
}

fun <T> Array<Array<out T>>.forEachDeep(operation: (outer: Array<out T>, inner: T) -> Unit) {
    for (outer in this) for (inner in outer)
        operation(outer, inner)
}

fun <T> Array<Array<out T>>.forEachDeepIndexed(operation: (i: Int, outer: Array<out T>, j: Int, inner: T) -> Unit) {
    for ((i, outer) in this.withIndex()) for ((j, inner) in outer.withIndex())
        operation(i, outer, j, inner)
}

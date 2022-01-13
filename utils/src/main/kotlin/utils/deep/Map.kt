package utils.deep

fun <T, R> Iterable<Iterable<T>>.mapDeep(operation: (outer: Iterable<T>, inner: T) -> R): List<List<R>> {
    return this.map { outer ->
        outer.map { inner ->
            operation(outer, inner)
        }
    }
}

fun <T, R> Iterable<Iterable<T>>.mapDeep(operation: (it: T) -> R): List<List<R>> {
    return this.map { outer ->
        outer.map { inner ->
            operation(inner)
        }
    }
}

fun <T, R> Iterable<Iterable<T>>.mapDeepIndexed(operation: (i: Int, outer: Iterable<T>, j: Int, inner: T) -> R): List<List<R>> {
    return this.mapIndexed { i, outer ->
        outer.mapIndexed { j, inner ->
            operation(i, outer, j, inner)
        }
    }
}

fun <T, R> Array<Array<T>>.mapDeep(operation: (outer: Array<T>, inner: T) -> R): List<List<R>> {
    return this.map { outer ->
        outer.map { inner ->
            operation(outer, inner)
        }
    }
}

fun <T, R> Array<Array<T>>.mapDeep(operation: (it: T) -> R): List<List<R>> {
    return this.map { outer ->
        outer.map { inner ->
            operation(inner)
        }
    }
}

fun <T, R> Array<Array<T>>.mapDeepIndexed(operation: (i: Int, outer: Array<T>, j: Int, inner: T) -> R): List<List<R>> {
    return this.mapIndexed { i, outer ->
        outer.mapIndexed { j, inner ->
            operation(i, outer, j, inner)
        }
    }
}
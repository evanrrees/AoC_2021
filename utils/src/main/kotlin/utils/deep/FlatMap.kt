package utils.deep

/**
 * Returns a single list of all elements yielded from results of [transform] function being invoked on each element of each collection in the receiver.
 */
fun <T, S: Iterable<T>, R> Iterable<S>.flatMapDeep(transform: (outer: S, inner: T) -> R): List<R> {
    return this.flatMap { outer ->
        outer.map { inner ->
            transform(outer, inner)
        }
    }
}

/**
 * Returns a single list of all elements yielded from results of [transform] function being invoked on each element of each collection in the receiver.
 */
fun <T, S: Iterable<T>, R> Iterable<S>.flatMapDeep(transform: (it: T) -> R): List<R> {
    return this.flatMap { outer ->
        outer.map { inner ->
            transform(inner)
        }
    }
}

fun <T, S: Iterable<T>, R> Iterable<S>.flatMapDeepIndexed(transform: (i: Int, outer: S, j: Int, inner: T) -> R): List<R> {
    return this.flatMapIndexed { i, outer ->
        outer.mapIndexed { j, inner ->
            transform(i, outer, j, inner)
        }
    }
}

fun <T, R> Array<Array<T>>.flatMapDeep(transform: (outer: Array<T>, inner: T) -> R): List<R> {

    return this.flatMap { outer ->
        outer.map { inner ->
            transform(outer, inner)
        }
    }
}

fun <T, R> Array<Array<T>>.flatMapDeep(transform: (it: T) -> R): List<R> {
    return this.flatMap { outer ->
        outer.map { inner ->
            transform(inner)
        }
    }
}

fun <T, R> Array<Array<T>>.flatMapDeepIndexed(operation: (i: Int, outer: Array<T>, j: Int, inner: T) -> R): List<R> {
    return this.flatMapIndexed { i, outer ->
        outer.mapIndexed { j, inner ->
            operation(i, outer, j, inner)
        }
    }
}
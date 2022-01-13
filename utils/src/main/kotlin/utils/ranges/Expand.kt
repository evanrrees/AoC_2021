package utils.ranges

// TODO: 12/27/21 move this to another package

fun <T, R, V> Iterable<T>.expand(other: Iterable<R>, transform: (a: T, b: R) -> V): List<V> {
    return flatMap { a -> other.map { b -> transform(a, b) } }
}

infix fun <T, R> Iterable<T>.expand(other: Iterable<R>): List<Pair<T, R>> {
    return flatMap { a -> other.map { b -> a to b } }
}

fun <T, R, V> Iterable<T>.expand(other: Array<R>, transform: (a: T, b: R) -> V): List<V> {
    return flatMap { a -> other.map { b -> transform(a, b) } }
}

infix fun <T, R> Iterable<T>.expand(other: Array<R>): List<Pair<T, R>> {
    return flatMap { a -> other.map { b -> a to b } }
}

fun <T, R, V> Array<out T>.expand(other: Array<R>, transform: (a: T, b: R) -> V): List<V> {
    return flatMap { a -> other.map { b -> transform(a, b) } }
}

infix fun <T, R> Array<out T>.expand(other: Array<R>): List<Pair<T, R>> {
    return flatMap { a -> other.map { b -> a to b } }
}

fun <T, R, V> Array<out T>.expand(other: Iterable<R>, transform: (a: T, b: R) -> V): List<V> {
    return flatMap { a -> other.map { b -> transform(a, b) } }
}

infix fun <T, R> Array<out T>.expand(other: Iterable<R>): List<Pair<T, R>> {
    return flatMap { a -> other.map { b -> a to b } }
}
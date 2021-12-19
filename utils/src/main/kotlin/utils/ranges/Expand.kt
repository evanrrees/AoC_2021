package utils.ranges

fun <T, R, V> Iterable<T>.expand(other: Iterable<R>, transform: (Pair<T, R>) -> V): List<V> =
    expand(other).map(transform)

infix fun <T, R> Iterable<T>.expand(other: Iterable<R>): List<Pair<T, R>> = flatMap { a -> other.map { b -> a to b } }
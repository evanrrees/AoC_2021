package utils.ranges

fun <T, R, V> Iterable<T>.expand(other: Iterable<R>, transform: (a: T, b: R) -> V): List<V> =
    flatMap { a -> other.map { b -> transform(a, b) } }

fun <T, R, V> Iterable<T>.expand(other: Iterable<R>, transform: (pair: Pair<T, R>) -> V): List<V> =
    expand(other).map(transform)

infix fun <T, R> Iterable<T>.expand(other: Iterable<R>): List<Pair<T, R>> = flatMap { a -> other.map { b -> a to b } }

fun <T, R, V> Array<T>.expand(other: Array<R>, transform: (a: T, b: R) -> V): List<V> =
    flatMap { a -> other.map { b -> transform(a, b) } }

fun <T, R, V> Array<T>.expand(other: Array<R>, transform: (pair: Pair<T, R>) -> V): List<V> =
    expand(other).map(transform)

infix fun <T, R> Array<T>.expand(other: Array<R>): List<Pair<T, R>> = flatMap { a -> other.map { b -> a to b } }
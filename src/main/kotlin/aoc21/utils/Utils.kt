package aoc21.utils

fun <T> MutableList<T>.shiftRight(n: Int = 1) = repeat(n) { add(0, removeLast()) }

fun <T> MutableList<T>.shiftLeft(n: Int = 1) = repeat(n) { add(removeFirst()) }

infix fun Int.progressionTo(other: Int) = IntProgression.fromClosedRange(this, other, if (this <= other) 1 else -1)
fun <T> List<T>.toPair(): Pair<T, T> = component1() to component2()

infix fun <T, R> Iterable<T>.expand(other: Iterable<R>): List<Pair<T, R>> = flatMap { a -> other.map { b -> a to b } }
fun <T, R, V> Iterable<T>.expand(other: Iterable<R>, transform: (Pair<T, R>) -> V): List<V> =
    expand(other).map(transform)

infix operator fun String.times(n: Int) = repeat(n)
infix operator fun String.minus(other: String) = removeSuffix(other)
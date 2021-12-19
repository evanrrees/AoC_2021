package utils

fun <T> List<T>.toPair(): Pair<T, T> {
    check(size == 2) { "Too many elements." }
    return component1() to component2()
}
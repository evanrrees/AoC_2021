package aoc21.utils

fun <T> MutableList<T>.shiftRight(n: Int = 1) = repeat(n) { add(0, removeLast()) }
fun <T> MutableList<T>.shiftLeft(n: Int = 1) = repeat(n) { add(removeFirst()) }
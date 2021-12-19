package utils

fun <T> MutableList<T>.cycleRight(n: Int = 1) = repeat(n) { add(0, removeLast()) }
fun <T> MutableList<T>.cycleLeft(n: Int = 1) = repeat(n) { add(removeFirst()) }
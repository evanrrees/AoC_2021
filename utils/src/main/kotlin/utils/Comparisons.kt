package utils

inline fun <T> maxOf(a: T, b: T, crossinline comparator: (T) -> Int): T {
    return maxOf(a, b, compareBy(comparator))
}

inline fun <T> minOf(a: T, b: T, crossinline comparator: (T) -> Int): T {
    return minOf(a, b, compareBy(comparator))
}
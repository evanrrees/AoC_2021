package utils

infix operator fun String.minus(other: String) = removeSuffix(other)
infix operator fun String.times(n: Int) = repeat(n)
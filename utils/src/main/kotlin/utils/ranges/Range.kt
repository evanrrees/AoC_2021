package utils.ranges

@JvmName("rangeOfChar")
fun Iterable<Char>.range(): CharRange = minOf { it } .. maxOf { it }

@JvmName("rangeOfByte")
fun Iterable<Byte>.range(): IntRange = minOf { it } .. maxOf { it }

@JvmName("rangeOfShort")
fun Iterable<Short>.range(): IntRange = minOf { it } .. maxOf { it }

@JvmName("rangeOfInt")
fun Iterable<Int>.range(): IntRange = minOf { it } .. maxOf { it }

@JvmName("rangeOfLong")
fun Iterable<Long>.range(): LongRange = minOf { it } .. maxOf { it }

infix fun Int.progressionTo(other: Int) = IntProgression.fromClosedRange(this, other, if (this <= other) 1 else -1)
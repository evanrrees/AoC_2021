package utils.math

import kotlin.math.abs

val Short.Companion.ZERO: Short get() = 0
val Byte.Companion.ZERO: Byte get() = 0

/** Return the greatest common denominator of two values. */
fun gcd(a: Byte, b: Byte): Byte = gcd(b.toInt(), a % b).toByte()
/** Return the greatest common denominator of two values. */
fun gcd(a: Short, b: Short): Short = gcd(b.toInt(), a % b).toShort()
/** Return the greatest common denominator of two values. */
tailrec fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
/** Return the greatest common denominator of two values. */
tailrec fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

/** Return the least common multiple of two values, */
fun lcm(a: Byte, b: Byte): Byte = (abs(a.toInt() * b.toInt()) / gcd(a, b)).toByte()
/** Return the least common multiple of two values, */
fun lcm(a: Short, b: Short): Short = (abs(a.toInt() * b.toInt()) / gcd(a, b)).toShort()
/** Return the least common multiple of two values, */
fun lcm(a: Int, b: Int): Int = abs(a * b) / gcd(a, b)
/** Return the least common multiple of two values, */
fun lcm(a: Long, b: Long): Long = abs(a * b) / gcd(a, b)
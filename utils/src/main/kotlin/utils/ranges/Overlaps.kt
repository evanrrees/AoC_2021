package utils.ranges

// TODO: 12/27/21 create tests

infix fun IntRange.overlaps(other: IntRange) =
    first in other || last in other || other.first in this || other.last in this
infix fun LongRange.overlaps(other: LongRange) =
    first in other || last in other || other.first in this || other.last in this
infix fun CharRange.overlaps(other: CharRange) =
    first in other || last in other || other.first in this || other.last in this

fun CharRange.contains(other: CharRange) = other.first in this && other.last in this
fun IntRange .contains(other: IntRange)  = other.first in this && other.last in this
fun LongRange.contains(other: LongRange) = other.first in this && other.last in this
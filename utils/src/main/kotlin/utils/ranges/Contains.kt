package utils.ranges

operator fun CharRange.contains(other: CharRange) = other.first in this && other.last in this
operator fun IntRange .contains(other: IntRange)  = other.first in this && other.last in this
operator fun LongRange.contains(other: LongRange) = other.first in this && other.last in this
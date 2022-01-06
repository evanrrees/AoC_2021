package utils.ranges

infix fun Char.rangeWith(other: Char): CharRange = if (this < other) this .. other else other .. this
infix fun Int.rangeWith(other: Int):   IntRange  = if (this < other) this .. other else other .. this
infix fun Long.rangeWith(other: Long): LongRange = if (this < other) this .. other else other .. this

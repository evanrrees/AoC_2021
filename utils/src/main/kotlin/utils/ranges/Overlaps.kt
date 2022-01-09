package utils.ranges

// TODO: 12/27/21 create tests

inline infix fun <reified T : Comparable<T>> ClosedRange<T>.overlaps(other: ClosedRange<T>): Boolean {
    if (start in other)             return true
    if (endInclusive in other)      return true
    if (other.start in this)        return true
    if (other.endInclusive in this) return true
    return false
}

inline infix fun <reified T : Comparable<T>> ClosedRange<T>.notOverlaps(other: ClosedRange<T>): Boolean {
    if (start in other)             return false
    if (endInclusive in other)      return false
    if (other.start in this)        return false
    if (other.endInclusive in this) return false
    return true
}
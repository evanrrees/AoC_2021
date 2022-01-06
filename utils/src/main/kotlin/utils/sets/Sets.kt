package utils.sets

public infix fun <T> Iterable<T>.or(other: Iterable<T>): Set<T> = this.union(other)
public infix fun <T> Iterable<T>.and(other: Iterable<T>): Set<T> = this.intersect(other.toSet())
public infix fun <T> Iterable<T>.xor(other: Iterable<T>): Set<T> {
    val set = this.toSet()
    val otherSet = other.toSet()
    return (set - otherSet) + (otherSet - set)
}

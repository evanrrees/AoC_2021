package utils.takeMiddleWhile

/**
 * Return list of elements left and right of [index] while elements match [predicate]
 *
 * @throws IllegalArgumentException if [index] is negative
 * @throws IndexOutOfBoundsException if there are fewer than [index] + 1 elements
 */
fun <T> Iterable<T>.takeMiddleWhile(index: Int, predicate: (T) -> Boolean): List<T> {
    require(index >= 0) { "Requested index $index is less than zero." }
    val new = ArrayList<T>()
    for ((idx, element) in this.withIndex())
        if (predicate(element))
            new.add(element)
        else if (idx <= index)
            new.clear()
        else
            break
    if (new.lastIndex < index)
        throw IndexOutOfBoundsException(index)
    return new
}
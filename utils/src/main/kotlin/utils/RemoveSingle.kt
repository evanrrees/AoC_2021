package utils

fun <T> MutableList<T>.removeSingle(predicate: (T) -> Boolean): T {
    var single: T? = null
    var found = false
    var foundAt = -1
    for ((index, element) in this.withIndex()) {
        if (predicate(element)) {
            if (found) throw IllegalArgumentException("Collection contains more than one matching element.")
            single = element
            foundAt = index
            found = true
        }
    }
    if (!found) throw NoSuchElementException("Collection contains no element matching the predicate.")
    this.removeAt(foundAt)
    @Suppress("UNCHECKED_CAST")
    return single as T
}

fun <T> MutableSet<T>.removeSingle(predicate: (T) -> Boolean): T {
    var single: T? = null
    var found = false
    for (element in this) {
        if (predicate(element)) {
            if (found) throw IllegalArgumentException("Collection contains more than one matching element.")
            single = element
            found = true
        }
    }
    if (!found) throw NoSuchElementException("Collection contains no element matching the predicate.")
    this.remove(single)
    @Suppress("UNCHECKED_CAST")
    return single as T
}
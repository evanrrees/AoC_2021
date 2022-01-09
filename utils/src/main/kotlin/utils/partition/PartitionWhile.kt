package utils.partition

inline fun <T> Iterable<T>.partitionWhile(predicate: (T) -> Boolean): Pair<List<T>, List<T>> {
    val first = ArrayList<T>()
    val second = this.toMutableList()
    while (second.isNotEmpty() && predicate(second.first()))
        first += second.removeFirst()
    return Pair(first, second)
}

inline fun <T> Iterable<T>.partitionWhile2(predicate: (T) -> Boolean): Pair<List<T>, List<T>> {
    val left = ArrayList<T>()
    val right = ArrayList<T>()
    var target = left
    for (element in this) {
        if (target == left && predicate(element)) {
            target = right
        }
        target.add(element)
    }
    return Pair(left, right)
}

inline fun <T> Iterable<T>.partitionWhileLast(predicate: (T) -> Boolean): Pair<List<T>, List<T>> {
    val first = ArrayList<T>()
    val second = this.toMutableList()
    while (predicate(second.last()))
        first += second.removeLast()
    return Pair(first, second)
}

inline fun <T> List<T>.takeLastWhile2(predicate: (T) -> Boolean): List<T> {
    if (isEmpty())
        return emptyList()
    val iterator = listIterator(size)
    while (iterator.hasPrevious()) {
        if (!predicate(iterator.previous())) {
            iterator.next()
            val expectedSize = size - iterator.nextIndex()
            if (expectedSize == 0) return emptyList()
            return ArrayList<T>(expectedSize).apply {
                while (iterator.hasNext())
                    add(iterator.next())
            }
        }
    }
    return toList()
}

inline fun <T> Iterable<T>.partitionWhileLast2(predicate: (T) -> Boolean): Pair<List<T>, List<T>> {
    val left = ArrayList<T>()
    val right = ArrayList<T>()
    var last = 0
    for ((i, element) in this.withIndex()) {
        if (predicate(element)) {
            last = i
            right.add(element)
        }
        left.add(element)
    }
    TODO()
    return Pair(left, right)
}
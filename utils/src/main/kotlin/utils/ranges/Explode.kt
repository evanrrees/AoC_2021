package utils.ranges

/*
This is sort of a combinatorial method.
It takes an iterable and recursively returns all combinations of `n` elements.
 */
private tailrec fun <T> Iterable<T>.explodeAndReduce(
    n: Int,
    result: List<T> = this.toList(),
    operation: (a: T, b: T) -> T
): List<T> {
    return if (n == 1) result
    else {
        explodeAndReduce(n - 1, flatMap { b -> result.map { a -> operation(a, b) } }, operation)
    }
}

tailrec fun <T, R> Iterable<T>.explodeAndFold(
    n: Int,
    initial: List<R>,
    operation: (accumulator: R, element: T) -> R
): List<R> {
    return if (n == 1) initial
    else {
        explodeAndFold(n - 1, flatMap { b -> initial.map { a -> operation(a, b) } }, operation)
    }
}

fun <T> Iterable<T>.explode(n: Int): List<List<T>> {
    val initial = listOf(this.toList())
    return this.explodeAndFold(n, initial) { accumulator, element -> accumulator + element }
}

fun <T> Iterable<T>.explodeRec(n: Int): List<List<T>> {

    val list = this.toList()
    val result = mutableListOf<List<T>>()

    fun _rec(i: Int, prev: List<T>) {
        if (i == 0) result += prev
        else for (element in this) _rec(i - 1, prev + element)
    }

    _rec(n, list)
    return result
}

fun <T, R> Pair<Iterable<T>, Iterable<R>>.forEachNested(operation: (a: T, b: R) -> Unit) {
    for (a in first) for (b in second)
        operation(a, b)
}

fun <T, R, S> Triple<Iterable<T>, Iterable<R>, Iterable<S>>.forEachNested(operation: (a: T, b: R, c: S) -> Unit) {
    for (a in first) for (b in second) for (c in third)
        operation(a, b, c)
}

fun <T> Collection<Iterable<T>>.forEachNested(operation: (Array<out T>) -> Unit) {
    fun _rec(i: Int = 0, vararg args: T) {
        if (i == size) operation(args)
        else {
            for (item in elementAt(i)) {
                _rec(i + 1, *args, item)
            }
        }
    }
    _rec()
}


//fun <T, R> Iterable<T>.explodeAndReduce(n: Int, initial: List<R>, operation: (a: R, b: T) -> R): List<R> {
//    return this.explodeAndReduce(n, initial, emptyList(), operation = operation)
//}
//
//
fun <T> Iterable<T>.explodeAndReduce(n: Int, operation: (a: T, b: T) -> T): List<T> {
    return this.explodeAndReduce(n, this.toList(), operation)
}
//
//fun <T> Iterable<T>.explodeAndReduce(): List<List<T>> {
//    val list: List<List<T>> = map { listOf(it) }
//    return this.explodeAndFold(list.size, list) { a: List<T>, b: T -> a + b }
//}
package aoc21.utils

fun <T> Collection<T>.second(): T                          = elementAt(2)
fun <T> Collection<T>.second(predicate: (T) -> Boolean): T = filter(predicate).second()
fun <T> Array<T>.second(): T                               = elementAt(2)
fun <T> Array<T>.second(predicate: (T) -> Boolean): T      = filter(predicate).second()
fun <T> Sequence<T>.second(): T                            = elementAt(2)
fun <T> Sequence<T>.second(predicate: (T) -> Boolean): T   = filter(predicate).second()

fun <T> Collection<T>.third(): T                           = elementAt(3)
fun <T> Collection<T>.third(predicate: (T) -> Boolean): T  = filter(predicate).third()
fun <T> Array<T>.third(): T                                = elementAt(3)
fun <T> Array<T>.third(predicate: (T) -> Boolean): T       = filter(predicate).third()
fun <T> Sequence<T>.third(): T                             = elementAt(3)
fun <T> Sequence<T>.third(predicate: (T) -> Boolean): T    = filter(predicate).third()


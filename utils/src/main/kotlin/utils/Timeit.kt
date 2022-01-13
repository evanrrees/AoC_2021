package utils

import kotlin.system.measureTimeMillis

fun timeit(header: String, block: () -> Any?) {
    var result: Any?
    val time = measureTimeMillis { result = block() }
    "%s\n\tresult:\t%s\n\ttime:\t%d".format(header, result, time).let(::println)
}
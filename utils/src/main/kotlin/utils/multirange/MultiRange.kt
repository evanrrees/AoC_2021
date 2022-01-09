package utils.multirange

import utils.ranges.*

abstract class MultiRange(vararg val dims: IntRange, val ndim: Int) {
    operator fun contains(other: MultiRange) =
        if (other.ndim > ndim) false else other.dims.zip(dims).all { (a, b) -> a in b }
    infix fun overlaps(other: MultiRange): Boolean {
        val lastSharedIndex = minOf(ndim, other.ndim) - 1
        for (i in 0 until lastSharedIndex) if (!(dims[i] overlaps other.dims[i])) return false
        return true
    }
    init {
        assert(dims.size == ndim) { "Incorrect number of dimensions. Expected $ndim, found ${dims.size}." }
    }
}

open class LineSegment(vararg dims: IntRange): MultiRange(*dims, ndim = 1) {
    val x get() = dims[0]
    operator fun component1() = x
}

open class Square(vararg dims: IntRange): MultiRange(*dims, ndim = 2) {
    val x get() = dims[0]
    val y get() = dims[1]
    operator fun component1() = x
    operator fun component2() = y
}

open class Cube(vararg dims: IntRange): MultiRange(*dims, ndim = 3) {
    val x get() = dims[0]
    val y get() = dims[1]
    val z get() = dims[2]
    operator fun component1() = x
    operator fun component2() = y
    operator fun component3() = z
}

open class Tesseract(vararg dims: IntRange): MultiRange(*dims, ndim = 4)
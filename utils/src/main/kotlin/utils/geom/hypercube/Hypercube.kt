package utils.geom.hypercube

import utils.ranges.*

interface Hypercube {

    val dims: Array<out IntRange>
    val n: Int

    operator fun contains(other: Hypercube): Boolean {
        if (other.n > n) return false
        val lastSharedIndex = minOf(n, other.n) - 1
        for (i in 0 .. lastSharedIndex)
            if (dims[i] notOverlaps other.dims[i])
                return false
        return true
    }

    infix fun overlaps(other: Hypercube): Boolean {
        val lastSharedIndex = minOf(n, other.n) - 1
        for (i in 0 .. lastSharedIndex)
            if (dims[i] notOverlaps other.dims[i])
                return false
        return true
    }

}

abstract class AbstractHypercube0(val value: Int): Hypercube {
    override val dims: Array<out IntRange> = arrayOf(value..value)
    override val n: Int = 0
}

abstract class AbstractHypercube1(vararg _dims: IntRange, _n: Int = 1) : Hypercube {
    final override val dims = _dims
    override val n = _n
    val xRange: IntRange get() = dims[0]
    val vertices: List<Vertex> by lazy {
        val result = mutableListOf<Vertex>()
        emptyList<Vertex>()
    }
    init {
        assert(dims.size == _n) { "Incorrect number of dimensions. Expected $_n, found ${dims.size}." }
    }
}

abstract class AbstractHypercube2(vararg _dims: IntRange, n: Int = 2) : AbstractHypercube1(_dims = _dims, n) {
    val yRange: IntRange get() = dims[1]
    val edges: List<Edge> = TODO()
}

abstract class AbstractHypercube3(vararg _dims: IntRange, n: Int = 3) : AbstractHypercube2(_dims = _dims, n) {
    val zRange: IntRange get() = dims[2]
    val faces: List<Face> = TODO()
}

abstract class AbstractHypercube4(vararg _dims: IntRange, n: Int = 4) : AbstractHypercube3(_dims = _dims, n) {
    val wRange: IntRange get() = dims[3]
    val cells: List<Cell> = TODO()
}

class LineSegment(vararg dims: IntRange): AbstractHypercube1(_dims = dims)
class Square     (vararg dims: IntRange): AbstractHypercube2(_dims = dims)
class Cube       (vararg dims: IntRange): AbstractHypercube3(_dims = dims)
class Tesseract  (vararg dims: IntRange): AbstractHypercube4(_dims = dims)

interface HyperFace: Hypercube

class Vertex(value: Int): HyperFace, AbstractHypercube0(value)
class Edge(vararg dims: IntRange): HyperFace, AbstractHypercube1(_dims = dims)
class Face(vararg dims: IntRange): HyperFace, AbstractHypercube2(_dims = dims)
class Cell(vararg dims: IntRange): HyperFace, AbstractHypercube3(_dims = dims)
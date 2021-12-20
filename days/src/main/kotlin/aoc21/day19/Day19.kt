package aoc21.day19

import utils.accessors.*
import org.tinylog.kotlin.Logger
import utils.grids.Grid
import java.io.File
import utils.timeit
import kotlin.math.abs
import kotlin.math.sqrt

inline fun <reified T> Array<T>.swap(i: Int, j: Int) {
    val carry = this[i]
    this[i] = this[j]
    this[j] = carry
}

abstract class Point3 {
    abstract val points: Array<Int>
    val x get() = points.first()
    val y get() = points.second()
    val z get() = points.third()
    abstract fun copyOf(): Point3
    open fun rotateInPlace(axis: Int) {
        val i = (axis + 1) % 3
        val j = (axis + 2) % 3
        val carry = points[i]
        points[i] = points[j]
        points[j] = -carry
    }
    fun distanceTo(other: Point3): Double = points
        .zip(other.points) { a, b -> (a - b) }
        .sumOf { it * it }
        .let { sqrt(it.toDouble()) }

    fun manhattanTo(other: Point3): Long = points.zip(other.points) { a, b -> abs(a - b).toLong() }.sum()

    override fun toString() = "($x, $y, $z)"
    override fun equals(other: Any?) = "$this" == "$other"
    override fun hashCode() = "$this".hashCode()
}

class Beacon(override val points: Array<Int>): Point3() {
    constructor(x: Int, y: Int, z: Int): this(arrayOf(x, y, z))
    constructor(points: List<Int>): this(points.toTypedArray())
    var globalIndex: Int = 0
    override fun copyOf() = Beacon(points.copyOf()).also { it.globalIndex = globalIndex }
    operator fun minus(other: Point3): Beacon = Beacon(Array(points.size) { points[it] - other.points[it] })
        .also { it.globalIndex = globalIndex }
    operator fun plus(other: Point3): Beacon = Beacon(Array(points.size) { points[it] + other.points[it] })
        .also { it.globalIndex = globalIndex }
    fun reorient(o: Orientation): Beacon {
        val new = copyOf()
        o.turns.forEachIndexed { axis, n ->
            repeat(n) { new.rotateInPlace(axis) }
        }
        return new
    }
}

class Orientation(override val points: Array<Int> = arrayOf(1, 2, 3), val turns: Array<Int> = Array(3) { 0 }): Point3() {

    override fun rotateInPlace(axis: Int) {
        turns[axis]++
        super.rotateInPlace(axis)
    }

    override fun copyOf() = Orientation(points.copyOf(), turns.copyOf())

    companion object {

        val rotations = allRotations().groupBy { "$it" }
            .values.map { v -> v.minByOrNull { it.turns.sum() }!! }

        private fun allRotations(o: Orientation = Orientation(), axis: Int = 0): List<Orientation> =
            if (axis == 3) listOf(o)
            else {
                (0..3).flatMap {
                    if (it > 0) o.rotateInPlace(axis)
                    allRotations(o.copyOf(), axis + 1)
                }
            }

        fun find(beacon1a: Beacon, beacon1b: Beacon, beacon2a: Beacon, beacon2b: Beacon): Orientation? {
            val targetDelta = beacon1a - beacon1b
            for (o in rotations) {
                val r1 = beacon2a.reorient(o)
                val r2 = beacon2b.reorient(o)
                val newDelta = r1 - r2
                if (newDelta == targetDelta) {
                    (beacon1a - r1).points.copyInto(o.points)
                    return o
                }
            }
            return null
        }
        fun find(beacons1: Pair<Beacon, Beacon>, beacons2: Pair<Beacon, Beacon>): Orientation? {
            val (beacon1a, beacon1b) = beacons1
            val (beacon2a, beacon2b) = beacons2
            return find(beacon1a, beacon1b, beacon2a, beacon2b)
        }
    }
}


class Scanner(val id: Int) {
    val beacons = mutableListOf<Beacon>()
    val distances = mutableMapOf<Pair<Beacon, Beacon>, Double>()
    fun addBeacon(beacon: Beacon) {
        beacons.filter { beacon != it }.forEach {
            val d = beacon.distanceTo(it)
            distances[beacon to it] = d
            distances[it to beacon] = d
        }
        beacons += beacon
    }
    fun addBeacon(points: List<Int>) {
        addBeacon(Beacon(points))
    }
}

fun compareScanners(scanner1: Scanner, scanner2: Scanner, orientations: Grid<Orientation?>): Boolean {
    for (beacon1a in scanner1.beacons) {
        val d1 = scanner1.distances.filterKeys { it.first == beacon1a }
        for (beacon2a in scanner2.beacons) {
            val d2 = scanner2.distances.filterKeys { it.first == beacon2a }
            val overlap = d1.entries.mapNotNull { b1b ->
                d2.entries.firstOrNull { b2b -> b2b.value == b1b.value }?.let { b1b.key to it.key }
            }
            if (overlap.size >= 11) {
                val o = Orientation.find(overlap.first().first, overlap.first().second)!!
                orientations[scanner1.id, scanner2.id] = o.copyOf()
                val shared = overlap.flatMap { it.second.toList() }.toSet()
                (scanner2.beacons - shared).forEach {
                    val newBeacon = it.reorient(o) + o
                    scanner1.addBeacon(newBeacon)
                }
                return true
            }
        }
    }
    return false
}

fun combineScanners(scanner: Scanner, others: MutableList<Scanner>, orientations: Grid<Orientation?>): Scanner {
    return if (others.isEmpty()) scanner
    else {
        for (i in others.indices) {
            val shared = compareScanners(scanner, others[i], orientations)
            if (shared) {
                Logger.debug("Combined scanners ${scanner.id} and ${others[i].id}. ${others.size - 1} remaining")
                others.removeAt(i)
                return combineScanners(scanner, others, orientations)
            }
        }
        others += scanner
        combineScanners(others.removeFirst(), others, orientations)
    }
}

fun part1(scanners: List<Scanner>): Int {
    val o = Grid(scanners.size, scanners.size) { i, j ->
        if (i == j) Orientation(arrayOf(0, 0, 0)) else null
    }
    val map = combineScanners(scanners.first(), scanners.drop(1).toMutableList(), o)
    return map.beacons.size
}

fun part2(scanners: List<Scanner>): Long {
    val o = Grid(scanners.size, scanners.size) { i, j ->
        if (i == j) Orientation(arrayOf(0, 0, 0)) else null
    }
    combineScanners(scanners.first(), scanners.drop(1).toMutableList(), o)
    val ref = o.arr.first { row -> row.none { it == null } }
    var max = 0L
    for ((i, s1) in ref.withIndex()) {
        for (s2 in ref.drop(i + 1)) {
            max = maxOf(max, s1!!.manhattanTo(s2!!))
        }
    }
    return max
}

fun parseInput(inputFile: File): List<Scanner> {
    val scanners = mutableListOf<Scanner>()
    inputFile.forEachLine { line ->
        if (line.matches("--- scanner [0-9]+ ---".toRegex())) scanners += Scanner(line.split(' ').third().toInt())
        else if (line.isNotBlank()) scanners.last().addBeacon(line.split(',').map(String::toInt))
    }
    return scanners
}

fun main() {
    val inputFile = File("days/src/main/resources/Day19.txt")
    val scanners = parseInput(inputFile)
//    timeit("Part 1:") { part1(scanners) }
    timeit("Part 2:") { part2(scanners) }
}

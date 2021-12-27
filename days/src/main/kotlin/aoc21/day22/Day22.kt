package aoc21.day22

import utils.ranges.overlaps
import utils.ranges.contains
import utils.ranges.size
import java.io.File
import utils.timeit

internal data class CuboidRange(val x: IntRange, val y: IntRange, val z: IntRange, var on: Boolean = false) {

    val size: Long = x.size.toLong() * y.size * z.size

    companion object {
        operator fun invoke(string: String): CuboidRange {
            val (onoff, coords) = string.split(' ')
            return coords.split(',')
                .map { it.substringAfter('=').split("..").map(String::toInt) }
                .map { (a, b) -> a..b }
                .let { (x, y, z) ->
                    CuboidRange(x, y, z, onoff == "on")
                }
        }
    }

    infix fun overlaps(other: CuboidRange) = x overlaps other.x && y overlaps other.y && z overlaps other.z
    operator fun contains(other: CuboidRange) = this == other || other.x in x && other.y in y && other.z in z
    operator fun contains(cuboid: Cuboid) = cuboid.x in x && cuboid.y in y && cuboid.z in z
    fun copyOf() = CuboidRange(x, y, z, on)
    fun toList() = x.flatMap { a -> y.flatMap { b -> z.map { c -> Cuboid(a, b, c) } } }
    fun toSet() = toList().toSet()

    // intersection
    fun intersectWith(other: CuboidRange): CuboidRange? {
        return when {
            !(this overlaps other) -> null
            this in other          -> this.copyOf()
            else                   -> CuboidRange(
                maxOf(x.first, other.x.first) .. minOf(x.last, other.x.last),
                maxOf(y.first, other.y.first) .. minOf(y.last, other.y.last),
                maxOf(z.first, other.z.first) .. minOf(z.last, other.z.last),
                on
            )
        }
    }

    operator fun plus(other: CuboidRange): List<CuboidRange> {
        val result = mutableListOf(this)
        this.explodeWith(other).filter { it !in this }
        return result
    }

    operator fun minus(other: CuboidRange): List<CuboidRange> {
        return this.explodeWith(other).filter { it !in other }
    }

    // Union of all sectors. On/off is preserved
    private fun explodeWith(other: CuboidRange): List<CuboidRange> {
        if (!(this overlaps other)) return listOf(this, other)
        val xCoords = listOf(x.first, x.last, other.x.first, other.x.last).sorted().zipWithNext()
            .let { it.mapIndexed { idx, (a, b) -> when(idx) {
                0 -> a until b
                2 -> a + 1 .. b
                else -> a .. b
            } } }
        val yCoords = listOf(y.first, y.last, other.y.first, other.y.last).sorted().zipWithNext()
            .let { it.mapIndexed { idx, (a, b) -> when(idx) {
                0 -> a until b
                2 -> a + 1 .. b
                else -> a .. b
            } } }
        val zCoords = listOf(z.first, z.last, other.z.first, other.z.last).sorted().zipWithNext()
            .let { it.mapIndexed { idx, (a, b) -> when(idx) {
                0 -> a until b
                2 -> a + 1 .. b
                else -> a .. b
            } } }

        val result = mutableListOf<CuboidRange>()
        for (xr in xCoords) for (yr in yCoords) for (zr in zCoords) {
            val cr = CuboidRange(xr, yr, zr, on)
            if (cr in other) cr.on = other.on
            if (cr in other || cr in this) result += cr
        }
        return result.filter { it.on }
    }
    
}

internal data class Cuboid(val x: Int, val y: Int, val z: Int)

internal fun part1(cuboidRanges: List<CuboidRange>): Int {
    val target = CuboidRange(-50..50, -50..50, -50..50)
    return cuboidRanges
        .mapNotNull { it.intersectWith(target) }
        .fold(emptySet<Cuboid>()) { acc, it ->
            if (it.on) acc + it.toSet() else acc - it.toSet()
        }
        .size
}

internal class CuboidMultiRange {
    private val ranges = mutableSetOf<CuboidRange>()
    fun add(range: CuboidRange) {
        ranges.filter { it overlaps range }.forEach { other ->
            ranges.remove(other)
            ranges.addAll(other - range)
        }
        if (range.on) ranges += range
    }
    val size: Long get() = ranges.sumOf { it.size }
}

internal fun part2(cuboidRanges: List<CuboidRange>): Long {
    val result = CuboidMultiRange()
    cuboidRanges.forEach { result.add(it) }
    return result.size
}

internal fun parseInput(inputFile: File) = inputFile.readLines().map { CuboidRange(it) }

fun main() {
    val inputFile = File("days/src/main/resources/Day22.txt")
    val parsedInput = parseInput(inputFile)
    timeit("Part 1:") { part1(parsedInput) }
    timeit("Part 2:") { part2(parsedInput) }
}

package aoc21.day22

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day22KtTest {


    @Test
    fun part1Small() {
        val lines = listOf(
            "on x=10..12,y=10..12,z=10..12",
            "on x=11..13,y=11..13,z=11..13",
            "off x=9..11,y=9..11,z=9..11",
            "on x=10..10,y=10..10,z=10..10"
        )
        val ranges = lines.map { CuboidRange(it) }
        val folded = ranges.fold(emptySet<Cuboid>()) { acc, it ->
            if (it.on) acc + it.toSet() else acc - it.toSet()
        }
        assertEquals(39, folded.size)
    }

    @Test
    fun part1() {
        val inputFile = File("src/test/resources/day22/part1.txt")
        val cuboids = parseInput(inputFile)
        val expect = 590784
        val actual = part1(cuboids)
        assertEquals(expect, actual)
    }

    @Test
    fun cuboidRange() {
        val r1 = CuboidRange(-5..5, -5..5, -5..5, true)
        val r2 = CuboidRange(0..0, 0..0, 0..0, false)
        val r3 = CuboidRange(4..5, 4..5, 4..5, false)
        val r4 = CuboidRange(-10..10, -10..10, -10..10, true)
        val r5 = CuboidRange(-10..10, -10..10, -10..10, false)
        val result = CuboidMultiRange()
        result.add(r1)
        assertEquals(1331, result.size)
        result.add(r2)
        assertEquals(1330, result.size)
        result.add(r3)
        assertEquals(1322, result.size)
        result.add(r4)
        assertEquals(9261, result.size)
        result.add(r5)
        assertEquals(0, result.size)
        result
    }

    @Test
    fun part2() {
        val inputFile = File("src/test/resources/day22/part2.txt")
        val cuboids = parseInput(inputFile)
        val expect = 2758514936282235L
        val actual = part2(cuboids)
        assertEquals(expect, actual)
    }
}

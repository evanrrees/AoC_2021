package aoc21.day19

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import utils.accessors.second
import java.io.File

internal class Day19KtTest {

    val inputFile = File("src/test/resources/Day19.txt")
    val scanners = parseInput(inputFile)

    @Test
    fun distanceTo() {
        val s = Scanner(0).apply {
            addBeacon(listOf(0, 0, 0))
            addBeacon(listOf(1, 2, 2))
        }
        assertEquals(3.toDouble(), s.beacons.run { first().distanceTo(second()) })
    }

    @Test
    fun samePoints() {
        //-618,-824,-621
        //-537,-823,-458
//        val a0 = Beacon(0, 0, mutableListOf(-618,-824,-621))
//        val b0 = Beacon(0, 1, mutableListOf(-537,-823,-458))
        val s1 = Scanner(0).apply {
            addBeacon(listOf(-618,-824,-621))
            addBeacon(listOf(-537,-823,-458))
        }
        //686,422,578
        //605,423,415
//        val a1 = Beacon(1, 0, mutableListOf(686,422,578))
//        val b1 = Beacon(1, 1, mutableListOf(605,423,415))
        val s2 = Scanner(0).apply {
            addBeacon(listOf(686,422,578))
            addBeacon(listOf(605,423,415))
        }
        val d0 = s1.beacons.run { first().distanceTo(second()) }
        val d1 = s2.beacons.run { first().distanceTo(second()) }
        assertEquals(d0, d1)
    }

    @Test
    fun findOrientation() {
        val p1 = Beacon(-618,-824,-621) to Beacon(-537,-823,-458)
        val p2 = Beacon(686,422,578) to Beacon(605,423,415)
        val o = Orientation.find(p1, p2)!!
        o
    }

    @Test
    fun delta() {
//        val s1 = Scanner(0).apply {
//            addBeacon(listOf(-618,-824,-621))
//            addBeacon(listOf(-537,-823,-458))
//        }
//        val s2 = Scanner(0).apply {
//            addBeacon(listOf(686,422,578))
//            addBeacon(listOf(605,423,415))
//        }
        val b1 = Beacon(10, 10, 10)
        val b2 = Beacon(5, 5, 5)
        assertEquals(b1 - b2, b2)

    }

    @Test
    fun compareScanners() {
        val result = compareScanners(scanners.first(), scanners.second())
    }

    @Test
    fun reorient() {
        val s1 = Scanner(0).apply {
            addBeacon(listOf(-618,-824,-621))
            addBeacon(listOf(-537,-823,-458))
        }
        val s2 = Scanner(0).apply {
            addBeacon(listOf(686,422,578))
            addBeacon(listOf(605,423,415))
        }

    }

    @Test
    fun parseInput() {
        assertEquals(5, scanners.size)
        scanners.forEachIndexed { i, scanner ->
            assertEquals(i, scanner.id)
        }
    }

    @Test
    fun part1() {
        val expect = 79
        val actual = part1(scanners)
        assertEquals(expect, actual)
    }

    @Test
    fun part2() {
        val expect = 3621
        val actual = part2(scanners)
        assertEquals(expect, actual)
    }

}
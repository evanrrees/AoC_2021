package utils.grids

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class GridTest {

    @Test
    fun getSize() {
    }

    @Test
    fun getLastRowIndex() {
    }

    @Test
    fun getLastColIndex() {
    }

    @Test
    fun first() {
    }

    @Test
    fun last() {
    }

    @Test
    fun get() {
    }

    @Test
    fun testGet() {
    }

    @Test
    fun set() {
    }

    @Test
    fun testSet() {
    }

    @Test
    fun contains() {
    }

    @Test
    fun testEquals() {
        val grid1 = Grid(2, 2, 0)
        val grid2 = Grid(2, 2, 0)
        assertEquals(grid1, grid2)

        val grid3 = Grid(2, 2, 1)
        assertNotEquals(grid1, grid3)

        val grid4 = Grid<Int>(0, 0)
        assertNotEquals(grid1, grid4)

        val grid5 = Grid(3, 3, 0)
        assertNotEquals(grid1, grid5)
    }

    @Test
    fun testHashCode() {
    }

    @Test
    fun forEach() {
    }

    @Test
    fun forEachIndexed() {
    }

    @Test
    fun points() {
    }

    @Test
    fun pointSequence() {
    }

    @Test
    fun maxOf() {
    }

    @Test
    fun testToString() {
    }

    @Test
    fun getArr() {
    }

    @Test
    fun getRows() {
    }

    @Test
    fun getCols() {
    }
}
package aoc21.day23

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import utils.grids.Grid
import utils.grids.Point
import utils.grids.copyOf
import utils.timeit
import java.io.File

internal typealias Move = Pair<Board, Long>

internal enum class AmphipodType(val energy: Long) {
    Amber(1), Bronze(10), Copper(100), Desert(1000);
    val home = ordinal * 2 + 2
}

internal class Amphipod(val type: AmphipodType, val start: Point) {
    constructor(c: Char, point: Point): this(AmphipodType.values().single { it.name[0] == c }, point)
    val home get() = type.home
    override fun toString() = "Amphipod(type=$type, start=(${start.i}, ${start.j}))"
    val firstBurrow = Board.burrows.single { it.x == this.home && it.y == 1 }
    val secondBurrow = Board.burrows.single { it.x == this.home && it.y == 2 }
}

internal open class Board(
    val map: BiMap<Room, Amphipod> = HashBiMap.create(),
    var cost: Long = Int.MAX_VALUE.toLong(),
    var prev: Board? = null,
    var seen: Boolean = false
) {

    val deepCost: Long get() = cost + (prev?.deepCost ?: 0L)

    internal fun hasWon() = map.all { (rm, ap) -> rm.x == ap.home }

    override fun hashCode() = this.toString().hashCode()
    override fun equals(other: Any?) = this.toString() == other.toString()

    val nextMoves by lazy {
        map.keys.flatMap { src -> src.validMoves()?.map { dest -> move(src, dest) } ?: emptyList() }
    }

    fun add(amphipod: Amphipod): Board {
        val pt = rooms.single { it == amphipod.start }
        map[pt] = amphipod
        return this
    }

    private fun move(src: Room, dest: Room): Move {
        val cost: Long = src.manhattanDistanceTo(dest) * src.getOccupant()!!.type.energy
        val result by lazy { HashBiMap.create(map).also { it[dest] = it.remove(src)!! }.let(::Board) }
//            map.toMutableMap().also { it[dest] = it.remove(src)!! }.let(::Board) }
        result.prev = this
        result.cost = this.cost + cost
        return result to cost
    }

    fun Room.getOccupant() = map[this]
    fun Room.isOccupied() = this in map
    fun Room.isEmpty() = this !in map
    fun Room.validMoves(): Set<Room>? {
        val amph = getOccupant()
        return if (amph == null) null
        else if (this is Burrow) {
            if (amph.inSecondBurrow())
                emptySet()
            else if (amph.inFirstBurrow() && amph.getNeighbor().inSecondBurrow())
                emptySet()
            else {
                val (left, right) = halls.partition { it.isLeftOf(this) }
                left.takeLastWhile { it.isEmpty() }.toSet() + right.takeWhile { it.isEmpty() }
            }
        }
        else {
            if (amph.firstBurrow.isOccupied())
                emptySet()
            else if (pathTo(amph.firstBurrow).any { it.isOccupied() })
                emptySet()
            else if (amph.secondBurrow.isOccupied()) {
                if (amph.getNeighbor().inSecondBurrow())
                    setOf(amph.firstBurrow)
                else
                    emptySet()
            }
            else
                setOf(amph.secondBurrow)
        }

    }

    fun Amphipod.getNeighbor() = map.values.single { it.type == type && it.start != start }
    fun Amphipod.getPos() = map.inverse()[this]!!
    fun Amphipod.inFirstBurrow() = getPos() == firstBurrow
    fun Amphipod.inSecondBurrow() = getPos() == secondBurrow

    override fun toString(): String {
        val result = stringTemplate.copyOf()
        map.forEach { (pt, amph) ->
            result[pt.y + 1, pt.x + 1] = amph.type.name[0]
        }
        return result.arr.joinToString("\n") { it.joinToString("") }
    }

    companion object {

        internal sealed class Room(y: Int, x: Int): Point(i = y, j = x) {
            val adjacent = mutableSetOf<Room>()
            fun pathTo(other: Room): List<Room> = paths.getValue(this to other)
        }

        internal class Burrow(y: Int, x: Int): Room(y = y, x = x) {
            override fun toString() = "Burrow(x=$x, y=$y)"
        }

        internal class Hall(y: Int, x: Int): Room(y = y, x = x) {
            override fun toString() = "Hall(x=$x, y=$y)"
        }

        val burrows = listOf(
            Burrow(1, 2), Burrow(2, 2), Burrow(1, 4), Burrow(2, 4),
            Burrow(1, 6), Burrow(2, 6), Burrow(1, 8), Burrow(2, 8)
        )
        val halls = listOf(Hall(0, 0), Hall(0, 1), Hall(0, 3), Hall(0, 5), Hall(0, 7), Hall(0, 9), Hall(0, 10))
            .sortedBy { it.y }

        val rooms = burrows + halls
        val paths: Map<Pair<Room, Room>, List<Room>>
        init {
            burrows.groupBy { it.x }.values.forEach { room ->
                val (first, last) = room.sortedBy { it.y }
                last.adjacent += first
                first.adjacent += last
                val (left, right) = halls.partition { it.isLeftOf(first) }
                left.lastOrNull()?.let { first.adjacent += it; it.adjacent += first }
                right.firstOrNull()?.let { first.adjacent += it; it.adjacent += first }
            }
            halls.zipWithNext { a, b ->
                a.adjacent += b
                b.adjacent += a
            }
            paths = mutableMapOf()
            for (b in burrows) {
                for (h in halls) {
                    val path = findPath(b, h)
                    paths[b to h] = path.drop(1)
                    paths[h to b] = path.reversed().drop(1)
                }
            }
        }

        tailrec fun findPath(src: Room, dest: Room, path: List<Room> = listOf(src)): List<Room> {
            return if (src == dest) path
            else {
                val next = when {
                    dest in src.adjacent -> dest
                    src.y == 2 -> src.adjacent.single()
                    dest.isRightOf(src) ->
                        src.adjacent.filter { it.isRightOf(src) && (it.y == 0 || it.x == dest.x) }.minByOrNull { it.x }!!
                    else ->
                        src.adjacent.filter { it.isLeftOf(src) && (it.y == 0 || it.x == dest.x) }.maxByOrNull { it.x }!!
                }
                findPath(next, dest, path + next)
            }
        }

        val stringTemplate = Grid(5, 13) { i, j ->
            if (i > 2 && (j < 2 || j > 10)) ' '
            else if (i == 1 && j > 0 && j < 12) '.'
            else if (Point(i - 1, j - 1) in burrows) '.'
            else '#'
        }

    }
}

internal fun allVertices(board: Board): Set<Board> {
    val result = mutableSetOf<Board>()
    val stack = mutableListOf(board)
    while (stack.isNotEmpty()) {
        val next = stack.removeFirst()
        if (next !in result) {
            result.add(next)
            stack.addAll(next.nextMoves.map(Move::first))
        }
    }
    return result
}

internal fun part1(board: Board): Long {
    val queue = mutableListOf(board)
    val moves = mutableMapOf<Board, List<Move>>()
    val costs = mutableMapOf(board to 0L).withDefault { Int.MAX_VALUE.toLong() }
    val prev = mutableMapOf<Board, Board?>(board to null).withDefault { null }
    val bag = mutableSetOf(board)
    while (bag.isNotEmpty()) {
        val u = bag.minByOrNull { it.cost }!!
        bag.remove(u)
        val neighbors = moves.getOrPut(u) { u.nextMoves }
        for ((v, cost) in neighbors) {
            val alt = costs.getValue(u) + cost
            if (alt < costs.getValue(v)) {
                costs[v] = alt
                prev[v] = u
                bag += v
            }
        }
    }
    val winners = costs.filter { it.key.hasWon() }
    winners.size
    return costs.entries.single { it.key.hasWon() }.value
}

internal fun parseInput(inputFile: File): Board {
    val board = Board(cost = 0L)
    inputFile.readLines().forEachIndexed { y, s ->
        s.forEachIndexed { x, c ->
            if (c in 'A'..'D') board.add(Amphipod(c, Point(i = y - 1, j = x - 1)))
        }
    }
    return board
}

fun main() {
    val inputFile = File("days/src/main/resources/Day23.txt")
    val parsedInput = parseInput(inputFile)
    timeit("Part 1:") { part1(parsedInput) }
    //timeit("Part 2:") { part2(parsedInput) }
}


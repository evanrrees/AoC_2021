package aoc21.day23

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import utils.grids.Grid
import utils.grids.Point
import utils.grids.copyOf
import utils.timeit
import java.io.File

//internal typealias Move = Pair<Board, Long>

internal enum class AmphipodType(val energy: Long) {
    Amber(1), Bronze(10), Copper(100), Desert(1000);
    val home = ordinal * 2 + 2
}

internal data class Amphipod(val type: AmphipodType, val start: Point) {
    constructor(c: Char, point: Point): this(AmphipodType.values().single { it.name[0] == c }, point)
    val home get() = type.home
    override fun toString() = "Amphipod(type=$type, start=(${start.i}, ${start.j}))"
    val firstBurrow = Board.burrows.single { it.x == this.home && it.y == 1 }
    val secondBurrow = Board.burrows.single { it.x == this.home && it.y == 2 }
}

internal open class Board(
    val map: BiMap<Room, Amphipod> = HashBiMap.create(),
    var cost: Long = Long.MAX_VALUE,
    var prev: Board? = null
) {

    internal fun hasWon() = map.all { (rm, ap) -> rm.x == ap.home }

    override fun hashCode() = this.toString().hashCode()
    override fun equals(other: Any?) = this.toString() == other.toString()

    val nextMoves by lazy {
        val moves = mutableListOf<Board>()
        for (src in map.keys) {
            src.validMoves()?.forEach { dest ->
                val new = move(src, dest)
                val ref = states.getOrPut(new) { new }
                moves.add(ref)
            }
        }
        moves
    }

    fun add(amphipod: Amphipod): Board {
        val pt = rooms.single { it == amphipod.start }
        map[pt] = amphipod
        return this
    }

    private fun move(src: Room, dest: Room): Board {
        val newMap = HashBiMap.create(map)
        newMap[dest] = newMap.remove(src)!!
        return Board(newMap)
    }

    fun distanceTo(other: Board): Long {
        val src = map.keys.minus(other.map.keys).single()
        val dest = other.map.keys.minus(map.keys).single()
        return src.manhattanDistanceTo(dest) * src.getOccupant()!!.type.energy
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
            else if (pointAbove() in map)
                emptySet()
            else {
                val (left, right) = halls.partition { it.isLeftOf(this) }
                left.takeLastWhile { it.isEmpty() }.toSet() + right.takeWhile { it.isEmpty() }
            }
        }
        else if (this is Hall) {
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
        } else {
            null
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

        val states = mutableMapOf<Board, Board>()

        fun initStates(start: Board) {
            start.nextMoves.takeIf { it.isNotEmpty() }?.forEach(::initStates) ?: return
        }
    }
}

//internal fun allVertices(board: Board): Set<Board> {
//    val result = mutableSetOf<Board>()
//    val stack = mutableListOf(board)
//    while (stack.isNotEmpty()) {
//        val next = stack.removeFirst()
//        if (next !in result) {
//            result.add(next)
//            stack.addAll(next.nextMoves.map(Move::first))
//        }
//    }
//    return result
//}

//internal fun part1fs(board: Board): Long {
//    var winningScore = Long.MAX_VALUE
//    fun dfs(next: Board, score: Long = 0L) {
//        if (next.hasWon()) {
//            winningScore = minOf(score, winningScore)
//        } else {
//            for ((b, s) in next.nextMoves) {
//                dfs(b, score + s)
//            }
//        }
//    }
//    dfs(board)
//    return winningScore
//}

internal fun part1(board: Board): Long {
//    Board.initStates(board)
    val bag = mutableSetOf(board)
//    val states = Board.states.toMap()
    board.cost = 0L
    while (bag.isNotEmpty()) {
        val u = bag.minByOrNull { it.cost }!!
        for (v in u.nextMoves) {
            val alt = u.cost + u.distanceTo(v)
            if (alt < v.cost) {
                v.cost = alt
                v.prev = u
                bag += v
            }
        }
        bag.remove(u)
    }

    val winner = Board.states.keys.single { it.hasWon() }
    return winner.cost
}

internal fun parseInput(inputFile: File): Board {
    val board = Board()
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


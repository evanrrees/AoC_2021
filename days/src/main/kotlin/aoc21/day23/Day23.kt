package aoc21.day23

import java.io.File
import utils.timeit
import utils.grids.Point
import utils.ranges.rangeWith
import kotlin.math.pow

typealias Amph = Triple<Char, Int, Int>
val Amph.pos get() = Point(second, third)
val Amph.energy get() = 10.0.pow(first - 'A').toLong()
val Amph.targetRoom get() = (first - 'A') * 2 + 2

internal typealias State = Map<Point, Board.Amphipod>

internal data class Board(
    val board: State,
    var cost: Long = Int.MAX_VALUE.toLong(),
    var prev: Board? = null,
    var visited: Boolean = false
) {
    constructor(amphipods: List<Amphipod>): this(amphipods.associateBy { it.position })
    constructor(board: State, pair: Pair<Point, Amphipod>): this(board + pair)
    constructor(board: State, pt: Point, amph: Amphipod): this(board, pt to amph)

    fun getPath(): List<Board> = listOf(this) + (prev?.getPath() ?: emptyList())
    fun getPathCost(): Long = prev?.let { it.distanceTo(this) + it.getPathCost() } ?: 0

    fun distanceTo(other: Board): Long {
        val (pt1, ap1) = board.entries.single { it.key !in other.board }
        val (pt2, ap2) = other.board.entries.single { it.key !in board }
        assert(ap1.type == ap2.type)
        return pt1.manhattanDistanceTo(pt2) * ap1.type.energy
    }

    val isWinner = board.all { (pt, amph) -> pt.x == amph.targetRoom }

    fun Point.canMoveTo(dest: Point): Boolean {
        if (dest in board) return false
        if (this in rooms) {
            if (this.pointAbove() in board) return false
        }
        if (dest in rooms) {
            val amph = board.getValue(this)
            // can only move to target room
            if (dest.x != amph.targetRoom) return false
            // can't move out of target room
            if (this == amph.lastTarget) return false
            // target room is blocked
            if (amph.firstTarget in board) return false
            board[amph.lastTarget]?.targetRoom?.let {
                if (it != amph.targetRoom) return false
            }
        }
        val hallRange = x rangeWith dest.x
        if (board.keys.any { it.inHall() && it.x in hallRange }) return false
        return true
    }
    private fun Point.canAccessHallway() = y == 1 || pointAbove() !in board
    private fun Point.inHall() = y == 0
    private fun Point.canMoveHome(): Boolean {
        val amph = board.getValue(this)
        if (amph.firstTarget in board) return false
        if (amph.lastTarget in board && board.getValue(amph.lastTarget).targetRoom != amph.targetRoom) return false
        if (board.keys.any { it != this && it.inHall() && it.x in x rangeWith amph.targetRoom })
            return false
        if (!inHall() && pointAbove() in board) return false
        return true
    }

    fun moves(): List<Board> {
        val boards = mutableListOf<Board>()
        if (!isWinner) {
            for ((pt, ap) in board) {
                if (pt == ap.lastTarget) continue
                val others = board - pt
                if (pt.inHall()) {
                    if (others.keys.any { it.inHall() && it.x in pt.x rangeWith ap.targetRoom }) {
                        continue
                    } else if (ap.firstTarget !in others) {
                        if (ap.lastTarget !in others) {
                            boards += Board(others, ap.lastTarget, ap)
                        } else if (others.getValue(ap.lastTarget).type == ap.type) {
                            boards += Board(others, ap.firstTarget, ap)
                        }
                    }
                } else if (pt.canAccessHallway()) {
                    val (left, right) = hall.partition { it.x < pt.x }
                    left.takeLastWhile { it !in others }.forEach { boards += Board(others, it, ap) }
                    right.takeWhile { it !in others }.forEach { boards += Board(others, it, ap) }
                }
            }
        }
        return boards
    }

    override fun toString(): String {
        val grid = template.map { it.copyOf() }
        board.forEach { (i, j), v -> grid[i + 1][j + 1] = v.type.name.first() }
        return grid.joinToString("\n") { it.joinToString("") }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Board) return false
        if (board != other.board) return false
        return true
    }

    override fun hashCode(): Int {
        return board.hashCode()
    }

    inner class Amphipod(val type: AmphipodType, val position: Point, private val start: Point = position) {
        constructor(c: Char, i: Int, j: Int):
                this(AmphipodType.values().single { it.name.startsWith(c) }, rooms.single { it == Point(i, j) })
        val targetRoom get() = type.room
        val roomPoints get() = rooms.filter { it.x == targetRoom }
        val firstTarget get() = roomPoints[0]
        val lastTarget get() = roomPoints[1]
        val counterpart: Amphipod by lazy {
            board.values.single { it != this && it.type == this.type }
        }
        val isSettled get() = position == lastTarget || position == firstTarget && counterpart.position == lastTarget
        fun moveTo(point: Point): Amphipod { return Amphipod(type, point, start) }
        fun getOther() = board.filterValues { it.type == type && it.start != this.start }.values.single()
        fun canMoveTo(dest: Point): Boolean {
            // can't move if destination is blocked
            if (dest in board) return false
            // can't move if settled
            if (isSettled) return false
            if (this.position in rooms) {
                if (this.position.pointAbove() in board) return false
            }
            if (dest in rooms) {
                // can only move to target room
                if (dest.x != this.targetRoom) return false
                // can only move if first room is open
                if (firstTarget in board) return false
                // can only move to first room if second room occupied by counterpart
                if (dest == firstTarget && counterpart.position != lastTarget) return false }
            // check hall path is clear
            val hallRange = position.x rangeWith dest.x
            if (board.keys.any { it.inHall() && it.x in hallRange }) return false
            return true
        }
    }

    internal enum class AmphipodType(val energy: Long) {
        Amber(1), Bronze(10), Copper(100), Desert(1000);
        val room = ordinal * 2 + 2
    }

    object Builder {
        private val list = mutableListOf<Amphipod>()
        fun addAmphipod(c: Char, i: Int, j: Int) = list.add(builder.run { Amphipod(c, i, j) })
        operator fun invoke(block: Builder.() -> Unit): Board {
            list.clear()
            apply(block)
            return Board(list.associateBy { it.position })
        }
    }

    companion object {
        private val builder = Board(emptyMap())
//        fun amphipod(c: Char, i: Int, j: Int) = builder.run { Amphipod(c, i, j) }
        private val rooms = listOf(
            Point(2, 2), Point(2, 4), Point(2, 6), Point(2, 8),
            Point(1, 2), Point(1, 4), Point(1, 6), Point(1, 8)
        )
        private val hall = listOf(
            Point(0, 0), Point(0, 1), Point(0, 3), Point(0, 5), Point(0, 7), Point(0, 9), Point(0, 10),
        )
        private val points = rooms + hall
        private val template = """
            #############
            #...........#
            ###.#.#.#.###
              #.#.#.#.#  
              #########  
        """.trimIndent().split("\n").map { it.toCharArray() }.toList()
    }
}

internal fun allStates(start: Board): Set<Board> {
    val set = mutableSetOf<Board>()
    val stack = mutableListOf(start)
    while (stack.isNotEmpty()) {
        val next = stack.removeFirst()
        if (next !in set) {
            set.add(next)
            stack.addAll(next.moves())
        }
    }
    return set
}

internal fun part1(start: Board): Long {
//    val states = mutableSetOf(start)
    val moves = mutableMapOf<Board, List<Board>>()
    val costs = mutableMapOf(start to 0L)
    val q = mutableListOf(start)
    while (q.isNotEmpty()) {
        val u = q.removeFirst()
        if (u in moves) {
            val mvs = moves[u]!!

        }

        for (v in u.moves()) {
            val alt = u.cost + u.distanceTo(v)
            if (alt < v.cost) {
                v.cost = alt
                v.prev = u
            }
        }
    }

    val states = allStates(start)
    val queue = states.toMutableSet()
    val trash = mutableSetOf<Board>()
    start.cost = 0L
    while (queue.isNotEmpty()) {
        val u = queue.minByOrNull { it.cost }!!
        u.visited = true
        queue.remove(u)
        trash.add(u)
        for (v in u.moves()) {
            if (v.visited) continue
            val alt = u.cost + u.distanceTo(v)
            if (alt < v.cost) {
                v.cost = alt
                v.prev = u
            }
        }
    }

    val winner = states.single { it.isWinner }
    tailrec fun backtrace(state: Board, path: List<Board> = listOf(state)): List<Board> =
        if (state.prev == null)
            path
        else
            backtrace(state.prev!!, path + state.prev!!)
    val path = backtrace(winner).reversed()
    path.forEach {
        System.err.println("\n${it.cost}\n$it")
    }
    return winner.cost

}

internal fun part2(state: Board): Int {
    TODO()
}

internal fun parseInput(inputFile: File): Board {

    return Board.Builder {
        inputFile.readLines().forEachIndexed { i, s ->
            s.forEachIndexed { j, c ->
                if (c in 'A'..'D') addAmphipod(c, i, j)
            }
        }
    }

//    val map = mutableMapOf<Point, Board.Amphipod>()
//    inputFile.readLines().forEachIndexed { i, s ->
//        s.forEachIndexed { j, c ->
//            if (c in 'A'..'D') {
//                map[Point(i - 1, j - 1)] = Board.amphipod(c, i, j)
//            }
//        }
//    }
//    return Board(map)

//    return inputFile.readLines().map { it.filter(Char::isLetter) }.filter { it.isNotEmpty() }
//        .flatMapIndexed { i, s -> s.mapIndexed { j, c -> Point(i + 1, j * 2 + 2) to Amphipod(c) } }
//        .toMap().let(::B)
}

fun main() {
    val inputFile = File("days/src/main/resources/Day23.txt")
    val parsedInput = parseInput(inputFile)
    timeit("Part 1:") { part1(parsedInput) }
    //timeit("Part 2:") { part2(parsedInput) }
}


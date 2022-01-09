package aoc21.day23

import utils.partitionWhile.partitionWhile
import utils.ranges.rangeWith
import utils.timeit
import java.io.File
import java.util.*
import kotlin.math.abs

internal enum class Amphipod {
    Amber, Bronze, Copper, Desert;
    val burrow = ordinal * 2 + 2
    val energy: Long by lazy { if (ordinal == 0) 1L else values()[ordinal - 1].energy * 10 }
    val value = name[0]
    companion object {
        operator fun invoke(char: Char) = values().single { it.name[0] == char }
    }
    override fun toString(): String = name.take(1)
}

internal class Move(
    val src: Int,
    val dest: Int,
    val mover: Amphipod,
    val distance: Long
) {
    override fun toString() = "%s: %-2d -> %-2d (%d)".format(mover, src, dest, distance)
    companion object {
        operator fun invoke(src:Int, dest: Int, amphipod: Amphipod, board: Board): Move {
            var distance = abs(src - dest).toLong()
            if (src in Board.burrows)
                distance += board.burrowSize - board.rooms[src].size + 1
            if (dest in Board.burrows)
                distance += board.burrowSize - board.rooms[dest].size
            distance *= amphipod.energy
            return Move(src, dest, amphipod, distance)
        }
        operator fun invoke(file: File): Board {
            val lists = List(11) { mutableListOf<Amphipod>() }
            file.readLines().forEach {
                it.forEachIndexed { j, c ->
                    if (c in 'A'..'D')
                        lists[j - 1] += Amphipod(c)
                }
            }
            val burrowSize = lists.maxOf { it.size }
            Board.nodes.clear()
            return Board(lists, burrowSize = burrowSize, cost = 0L)
        }
    }
}

internal class Board(val rooms: List<List<Amphipod>>,
            val burrowSize: Int,
            var cost: Long = Long.MAX_VALUE,
            var prev: Board? = null) {

    val hasWon: Boolean = burrows.all {
        rooms[it].run { size == burrowSize && all { amphi -> amphi.burrow == it } }
    }

    fun getMoves(): List<Pair<Board, Move>> =
        if (this.hasWon)
            emptyList()
        else {
            val worstMoves = mutableListOf<Triple<Amphipod, Int, Int>>()
            val bestMoves = mutableListOf<Triple<Amphipod, Int, Int>>()
            for ((src, stack) in rooms.withIndex()) {
                val amphipod = stack.firstOrNull()
                if (amphipod != null) {
                    val homeBurrow = rooms[amphipod.burrow]
                    if (homeBurrow.all { it.burrow == amphipod.burrow }) {
                        val dest = amphipod.burrow
                        if (src == dest) continue
                        val hallRange by lazy { src rangeWith dest }
                        if (
                            homeBurrow.size < burrowSize &&
                            !halls.any { it != src && it in hallRange && rooms[it].isNotEmpty() }
                        ) {
                            bestMoves += Triple(amphipod, src, dest)
                            continue
                        }
                    }
                    if (src in burrows) {
                        val (left, right) = halls.partitionWhile { it < src }
                        left.takeLastWhile { dest -> rooms[dest].isEmpty() }
                            .forEach { dest -> worstMoves += Triple(amphipod, src, dest) }
                        right.takeWhile { dest -> rooms[dest].isEmpty() }
                            .forEach { dest -> worstMoves += Triple(amphipod, src, dest) }
                    }
                }
            }

            bestMoves.ifEmpty { worstMoves }
                .map { (mover, src, dest) ->
                    val map = rooms.mapIndexed { index, list ->
                        when (index) {
                            src  -> list.drop(1)
                            dest -> listOf(mover) + list
                            else -> list
                        }
                    }
                    val move = Move(src, dest, mover, this)
                    val newBoard = Board(map, burrowSize).let { nodes.getOrPut(it) { it } }
                    if (newBoard.hasWon) return listOf(newBoard to move)
                    newBoard to move
                }
        }

    companion object {
        val nodes = mutableMapOf<Board, Board>()
        val halls = listOf(0, 1, 3, 5, 7, 9, 10)
        val burrows = listOf(2, 4, 6, 8)
    }

    override fun toString(): String = _string

    private val _string = run {
        val rows = rooms.maxOf { it.size } + 1
        val result = List(rows) { CharArray(11) { ' ' } }
        rooms.forEachIndexed { j, list ->
            if (j in halls)
                result[0][j] = list.singleOrNull()?.value ?: '_'
            else if (list.isEmpty())
                result[1][j] = '.'
            else
                list.forEachIndexed { i, amphipod -> result[i + 1][j] = amphipod.value }
        }
        result.joinToString("\n") { it.joinToString("") }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Board) return false
        if (_string != other._string) return false
        return true
    }

    override fun hashCode(): Int = _hash

    private val _hash = _string.hashCode()

}

internal fun runInteractive(file: File) {
    Board.nodes.clear()
    val start = parseInput(file)
    var board = start
    val s = Scanner(System.`in`)
    println("New game. Burrow size = ${start.burrowSize}")
    while (true) {
        println("\nBoard (cost=${board.cost}):\n$board")
        val moves = board.getMoves()
        if (board.hasWon) {
            println("You won!")
            break
        }
        if (moves.isEmpty()) {
            println("You lost!")
            board = board.prev ?: break
            continue
        }
        println("\nMoves:")
        moves.withIndex()
            .windowed(3, 3, true) {
                it.joinToString(" ") { (i, s) -> "$i)".padEnd(3) + " ${s.second}".padEnd(22) }
            }
            .forEach(::println)
        print("\nSelect a move: ")
        val input = if (moves.size > 1) s.nextInt() else {
            println("0 (only one move available)")
            0
        }
        if (input == -1) {
            board = board.prev ?: board.also {
                println("Can't go back - no previous element!")
            }
        } else {
            val (new, move) = moves[input]
            new.prev = board
            new.cost = board.cost + move.distance
            board = new
        }
    }
}

internal fun solve(file: File): Long {
    Board.nodes.clear()
    val start = parseInput(file)
    val queue = mutableListOf(start)
    while (queue.isNotEmpty()) {
        val u = queue.minByOrNull { it.cost }!!
        queue.remove(u)
        for ((v, move) in u.getMoves()) {
            val alt = u.cost + move.distance
            if (alt < v.cost) {
                v.cost = alt
                v.prev = u
                queue += v
            }
        }
    }
    val nodes = Board.nodes.keys
    val winners = nodes.filter { it.hasWon }
    val winner = winners.single()
    return winner.cost
}

internal fun parseInput(file: File): Board {
    val burrowSize: Int
    val temp = List(11) { mutableListOf<Amphipod>() }
    file.readLines().forEach {
        it.forEachIndexed { j, c ->
            if (c in 'A'..'D')
                temp[j - 1] += Amphipod(c)
        }
    }
    burrowSize = temp.filterIndexed { idx, _ -> idx in 2 .. 8 step 2 }
        .distinctBy { it.size }
        .single()
        .size
    Board.nodes.clear()
    return Board(temp, burrowSize = burrowSize, cost = 0L)
}

internal fun main() {
    val part1File = File("/Users/err87/repos/evanrr/AoC_2021/days/src/main/resources/day23/part1.txt")
    val part2File = File("/Users/err87/repos/evanrr/AoC_2021/days/src/main/resources/day23/part2.txt")
    timeit("Part 1:") { solve(part1File) }
    timeit("Part 2:") { solve(part2File) }
}


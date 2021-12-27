package aoc21.day21

import org.tinylog.kotlin.Logger
import java.io.File
import utils.timeit

internal data class ParsedInput(val player1: Player, val player2: Player)

internal class Player(pos: Int, var score: Int = 0) {
    var pos: Int = pos
        get() = if (field == 0) 10 else field
        set(value) {
            field = value
            field %= 10
        }

    fun move(n: Int) = apply {
        pos += n
        score += pos
    }

    override fun equals(other: Any?): Boolean = this === other || "$this" == "$other"
    override fun hashCode() = 31 * score + pos
    override fun toString() = "Player(pos=$pos, score=$score)"
}


internal val rolls = generateSequence(1) { (it % 100) + 1 }.windowed(3, 3, false, List<Int>::sum)

internal fun part1(parsedInput: ParsedInput): Int {
    val (p1, p2) = parsedInput
    val lastTurn = rolls
        .windowed(2, 2)
        .indexOfFirst { (a, b) ->
            Logger.debug("\nplayers: $p1, $p2\nrolls: $a, $b")
            p1.move(a).score >= 1000 || p2.move(b).score >= 1000
        }.plus(1)
    Logger.debug("$p1, $p2")
    val loser = if (p1.score >= 1000) p2 else p1
    Logger.debug("Loser: $loser")
    val numRolls = 6 * lastTurn - if (loser == p2) 3 else 0
    Logger.debug("Last turn: $lastTurn, Rolls: $numRolls")
    return loser.score * numRolls
}

internal tailrec fun triple(n: Int = 3, list: List<List<Int>> = List(n) { listOf(it + 1) }): List<List<Int>> =
    if (list.first().size == n) list
    else triple(n, (1..n).flatMap { list.map { l -> l + it } })

internal fun part2(parsedInput: ParsedInput): Long {

    data class State(val pos1: Int, val pos2: Int, val score1: Int, val score2: Int) {
        fun roll(n: Int) = ((pos1 + n) % 10).let { State(pos2, it, score2, score1 + it + 1) }
    }

    data class Answer(var a: Long = 0L, var b: Long = 0L) {
        operator fun times(int: Int) = Answer(a * int, b * int)
        infix fun shift(other: Answer) = Answer(a + other.b, b + other.a)
    }

    tailrec fun makeCounts(n: Int, list: List<Int> = List(n) { it + 1 }, i: Int = n): List<Int> =
        if (i == 1) list else makeCounts(n, (1..n).flatMap { list.map { l -> l + it } }, i - 1)

    val memo = mutableMapOf<State, Answer>()
    val rolls = makeCounts(3).groupingBy { it }.eachCount()

    fun countWins(state: State): Answer = when {
        state.score1 >= 21 -> Answer(1, 0)
        state.score2 >= 21 -> Answer(0, 1)
        else -> memo.getOrPut(state) {
            rolls.entries.fold(Answer()) { ans, (k, v) -> ans shift state.roll(k).let(::countWins) * v }
        }
    }

    return countWins(State(parsedInput.player1.pos - 1, parsedInput.player2.pos - 1, 0, 0)).let { (a, b) -> maxOf(a, b) }
}

internal fun parseInput(inputFile: File): ParsedInput {
    val (p1, p2) = inputFile.readLines().map { line -> line.takeLastWhile { it.isDigit() }.toInt().let(::Player) }
    return ParsedInput(p1, p2)
}

fun main() {
    val inputFile = File("days/src/main/resources/Day21.txt")
    val parsedInput = parseInput(inputFile)
    timeit("Part 1:") { part1(parsedInput) }
    timeit("Part 2:") { part2(parsedInput) }
}

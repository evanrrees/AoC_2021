package aoc21.day04

import java.io.File

data class Win(val score: Int, val turn: Int)

fun calculateWin(board: List<Int>, calls: List<Int>): Win {
    val turns = board.map(calls::indexOf)
    val bestRow = turns.windowed(5, 5).minOf { row -> row.maxOf { it } }
    val bestCol = turns.windowed(21).minOf { col -> col.filterIndexed { index, _ -> index % 5 == 0 }.maxOf { it } }
    val bestTurn = minOf(bestRow, bestCol)
    val score = turns.withIndex().filter { it.value > bestTurn }.sumOf { board[it.index] } * calls[bestTurn]
    return Win(score, bestTurn)
}

fun parseRow(row: String) = row.trim().split("\\s+".toRegex()).map(String::toInt)

fun parseRounds(row: String) = row.split(",").map(String::toInt)

fun selectBoard(inputFile: File, selector: Sequence<Win>.() -> Win?) = inputFile.run {
    val rounds = useLines { it.first() }.let(::parseRounds)
    useLines { lines ->
        lines.drop(2).windowed(5, 6, true) { calculateWin(it.flatMap(::parseRow), rounds) }
            .run(selector) ?: throw Exception("Selector returned null")
    }
}

fun part1(inputFile: File) = selectBoard(inputFile) { minByOrNull { it.turn } }.score

fun part2(inputFile: File) = selectBoard(inputFile) { maxByOrNull { it.turn } }.score

fun main(args: Array<String>) {
    println(part1(File(args[0])))
    println(part2(File(args[0])))
}
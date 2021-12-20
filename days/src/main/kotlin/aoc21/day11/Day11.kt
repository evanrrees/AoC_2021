package aoc21.day11

//import utils.utils.grids.forEachElementIndexed
import utils.arrays.deepCopyOf
import utils.io.readLongGrid
import utils.konsole.format
//import utils.utils.print.gridToString
//import utils.utils.grids.*

import java.io.File

//typealias BoolGrid = Array<Array<Boolean>>
//fun BoolGrid.all() = all { arr -> arr.all { it } }
//fun BoolGrid.reset() = forEach { arr -> arr.indices.forEach { arr[it] = false } }

typealias Octopi = Array<Array<Int>>
//fun Octopi.increase() = forEachElementIndexed { i, j, _ -> this[i, j]++ }
//fun Octopi.flash(hasFlashed: Array<Array<Boolean>>): Int {
//    var totalFlashes = 0
//    var flashesThisRound: Int
//    var round = 0
//    do {
//        round++
//        flashesThisRound = 0
//        forEachElementIndexed { i, j, it ->
//            if (it > 9 && !hasFlashed[i, j]) {
//                flashesThisRound++
//                hasFlashed[i, j] = true
//                for (m in maxOf(0, i - 1)..minOf(lastIndex, i + 1))
//                    for (n in maxOf(0, j - 1)..minOf(this[i].lastIndex, j + 1))
//                        this[m, n]++
//            }
//        }
//        totalFlashes += flashesThisRound
//    } while (flashesThisRound != 0)
//    return totalFlashes
//}
//
//fun Octopi.print(prefix: String = "") = gridToString("", header = prefix).let(::println)
//
//
//fun doSteps(octos: Octopi, steps: Int = 100, stopIfSynchronized: Boolean): Int {
//    val hasFlashed = Array(octos.size) { Array(octos[it].size) { false } }
//    var flashes = 0
//    repeat(steps) { step ->
//        octos.increase()
//        flashes += octos.flash(hasFlashed)
//        if (stopIfSynchronized && hasFlashed.all()) return step + 1
//        octos.setAll(0)
//        hasFlashed.setAll(false)
//    }
//    return flashes
//}

fun printOctos(octos: Array<Array<Long>>, prefix: String) {
    octos.joinToString("\n", "$prefix\n", "\n") { row ->
        row.joinToString("") { "$it".format { if (it == 0L) bright } }
    }.let(::println)
}

fun gridToCSV(octos: Array<Array<Long>>) = octos.joinToString("\n") { it.joinToString(",") }

data class Instruction(val step: Int, val round: Int, val i: Int, val j: Int, val stage: Int) {
    override fun toString() = "$step,$round,$i,$j,$stage"
    companion object {
        val header = "step,round,i,j,stage"
    }
}

fun part1(
    octos: Array<Array<Long>>,
    steps: Int = 100,
    instructionFile: File? = null,
    frameDir: String? = null,
    verbose: Boolean = false,
    stopIfSynchronized: Boolean = false
): Long {

//    return doSteps(octos, steps, stopIfSynchronized)
    
    if (verbose) printOctos(octos, "Before any steps:")
    val instructions = mutableListOf<Instruction>()
    val hasFlashed = Array(octos.size) { Array(octos[it].size) { false } }
    var flashes = 0L

    repeat(steps) { step ->

        // stage 1: Increase
        for (i in octos.indices) for (j in octos[i].indices) {
            octos[i][j]++
            if (instructionFile != null) instructions += Instruction(step, 0, i, j, 1)
        }

        // stage 2: Flash
        var flashesThisRound: Int
        var round = 0
        do {
            round++
            flashesThisRound = 0
            for (i in octos.indices) for (j in octos[i].indices) {
                if (octos[i][j] > 9 && !hasFlashed[i][j]) {
                    flashesThisRound++
                    hasFlashed[i][j] = true
                    for (m in maxOf(0, i - 1)..minOf(octos.lastIndex, i + 1))
                        for (n in maxOf(0, j - 1)..minOf(octos[i].lastIndex, j + 1)) {
                            octos[m][n]++
                            if (instructionFile != null) instructions += Instruction(step, round, i, j, 2)
                        }
                }
            }
            flashes += flashesThisRound
            frameDir?.let { File("$it/${step}_$round.csv") }?.writeText(gridToCSV(octos))
//            File("src/test/resources/day11/frames/${step}_$round.csv").writeText(gridToCSV(octos))
        } while (flashesThisRound != 0)

        if (stopIfSynchronized && octos.all { arr -> arr.all { it == 0L }}) {
            return step + 1L
        }

        // stage 3: Reset
        for (i in octos.indices) for (j in octos[i].indices) {
            hasFlashed[i][j] = false
            if (octos[i][j] > 9) {
                octos[i][j] = 0
                if (instructionFile != null) instructions += Instruction(step, 0, i, j, 3)
            }
        }
        if (verbose) printOctos(octos, "After step ${step + 1}:")
    }

    instructionFile?.bufferedWriter()?.use { writer ->
        writer.appendLine(Instruction.header)
        instructions.forEach { writer.appendLine("$it") }
    }

    return flashes
}

fun part2(octos: Array<Array<Long>>): Long {

    val hasFlashed = Array(octos.size) { Array(octos[it].size) { false } }
    var step: Long = 0
    do {
        // stage 1: Increase
        for (i in octos.indices) for (j in octos[i].indices) {
            hasFlashed[i][j] = false
            octos[i][j]++
        }

        // stage 2: Flash
        var anyHasFlashed: Boolean
        do {
            anyHasFlashed = false
            for (i in octos.indices) for (j in octos[i].indices) {
                if (octos[i][j] > 9 && !hasFlashed[i][j]) {
                    anyHasFlashed = true
                    hasFlashed[i][j] = true
                    for (m in maxOf(0, i - 1)..minOf(octos.lastIndex, i + 1))
                        for (n in maxOf(0, j - 1)..minOf(octos[i].lastIndex, j + 1))
                            octos[m][n]++
                }
            }
        } while (anyHasFlashed)

        // stage 3: Reset
        for (i in octos.indices) for (j in octos[i].indices) if (octos[i][j] > 9) octos[i][j] = 0

        step++
    } while (!hasFlashed.all { row -> row.all { it } })
    printOctos(octos, "After step $step:")
    return step
}

fun parseInput(inputFile: File) = readLongGrid(inputFile)

fun main() {
    val inputFile = File("src/main/resources/Day11.txt")
    val parsedInput = parseInput(inputFile)
    val result1 = part1(parsedInput.deepCopyOf(), stopIfSynchronized = true)
    println(result1)
    val result2 = part2(parsedInput.deepCopyOf())
    println(result2)
}

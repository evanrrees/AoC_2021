package aoc21.day16

import aoc21.utils.timeit
import java.io.File


enum class TypeID {
    Sum, Product, Min, Max, Literal, Greater, Less, Equal;
    companion object {
        operator fun get(index: Int) = values()[index]
        operator fun get(index: Long) = values()[index.toInt()]
    }
}

class Packet(val input: CharIterator) {

    val subpackets      = mutableListOf<Packet>()
    var bitLength       = 0
        get() = field + subpackets.sumOf { it.bitLength }
    val version         = nextInt(3).toLong()
    val typeId          = TypeID[nextInt(3)]
    val lengthTypeId    = if (typeId == TypeID.Literal) -1 else nextInt()
    val subpacketLength = if (lengthTypeId == -1) -1 else if (lengthTypeId == 0) nextInt(15) else nextInt(11)

    val value: Long
        get() = when (typeId) {
            TypeID.Sum     -> subpackets.sumOf { it.value }
            TypeID.Product -> subpackets.fold(1) { acc, p -> acc * p.value }
            TypeID.Min     -> subpackets.minOf { it.value }
            TypeID.Max     -> subpackets.maxOf { it.value }
            TypeID.Literal -> field
            TypeID.Greater -> if (subpackets.first().value > subpackets.last().value) 1 else 0
            TypeID.Less    -> if (subpackets.first().value < subpackets.last().value) 1 else 0
            TypeID.Equal   -> if (subpackets.first().value == subpackets.last().value) 1 else 0
        }

    init {
        var buffer = 0L
        if (typeId == TypeID.Literal) {
            var leading: Int
            do {
                leading = nextInt()
                buffer = (buffer shl 4) + nextInt(4)
            } while (leading != 0)
        } else if (lengthTypeId == 0) {
                var bitsRead = 0
                do {
                    subpackets += Packet(input)
                    bitsRead += subpackets.last().bitLength
                } while (bitsRead < subpacketLength)
        } else {
            repeat(subpacketLength) { subpackets += Packet(input) }
        }
        value = buffer
    }

    val deepVersion: Long
        get() = if (typeId == TypeID.Literal) version else version + subpackets.sumOf { it.deepVersion }
    val size: Int
        get() = if (typeId == TypeID.Literal) 1 else 1 + subpackets.size

    fun nextInt(n: Int = 1) = (1..n).fold(0) { it, _ -> (it shl 1) + input.next().digitToInt() }.also { bitLength += n }

}


fun part1(parsedInput: String): Long {
    return Packet(parsedInput.iterator()).deepVersion
}

fun part2(parsedInput: String): Long {
    return Packet(parsedInput.iterator()).value
}

fun String.hexToBinary() = map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("")

fun parseInput(inputFile: File) =
    inputFile.readLines().single().map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("")

fun main() {
    val inputFile = File("src/main/resources/Day16.txt")
    val parsedInput = parseInput(inputFile)
    timeit("Part 1:") { part1(parsedInput) }
    timeit("Part 2:") { part2(parsedInput) }
//    val result1 = part1(parsedInput)
//    println(result1)
    // val result2 = part2(parsedInput)
    // println(result2)
}

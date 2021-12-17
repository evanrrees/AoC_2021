package aoc21.day16

import aoc21.utils.timeit
import java.io.File


enum class TypeID {
    Sum, Product, Min, Max, Literal, Greater, Less, Equal;
    companion object {
        operator fun get(index: Long) = values()[index.toInt()]
    }
}

class Decoder(input: String) {

    val input = input.iterator()

    fun decodePacket(): Packet {
        val p = Packet()
        p.version = parseInt(p, 3)
        p.typeId = TypeID[parseInt(p, 3)]
        if (p.typeId == TypeID.Literal) {
            var leading: Int
            do {
                leading = parseInt(p, 1).toInt()
                p.value = p.value shl 4 or parseInt(p, 4)
            } while (leading != 0)
        }
        else {
            p.lengthTypeId = parseInt(p, 1).toInt()
            if (p.lengthTypeId == 0) {
                var packetsLength = 0
                val bitLength = parseInt(p, 15)
                do {
                    val subpacket = decodePacket()
                    packetsLength += subpacket.bitLength
                    p.addSubpacket(subpacket)
                } while (packetsLength < bitLength)
            } else {
                repeat(parseInt(p, 11).toInt()) { p.addSubpacket(decodePacket()) }
            }
        }
        return p
    }

    fun parseInt(p: Packet, l: Int): Long {
        var result = 0L
        repeat(l) { result = (result shl 1) + input.next().digitToInt() }
        p.bitLength += l
        return result
    }

    inner class Packet(var version: Long = 0L, var typeId: TypeID = TypeID.Literal) {
        var value: Long = 0L
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
        var lengthTypeId: Int? = null
        var bitLength: Int = 0
        private val subpackets = mutableListOf<Packet>()
        val deepVersion: Long
            get() = if (typeId == TypeID.Literal) version else version + subpackets.sumOf { it.deepVersion }
        val size: Int
            get() = if (typeId == TypeID.Literal) 1 else 1 + subpackets.size
        val deepSize: Int
            get() = if (typeId == TypeID.Literal) 1 else 1 + subpackets.sumOf { it.deepSize }

        fun addSubpacket(sub: Packet) {
            subpackets += sub
            bitLength += sub.bitLength
        }

    }
}

fun part1(parsedInput: String): Long {
    val decoder = Decoder(parsedInput)
    val packet = decoder.decodePacket()
    return packet.deepVersion
}

fun part2(parsedInput: String): Long {
    val decoder = Decoder(parsedInput)
    val packet = decoder.decodePacket()
    return packet.value
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

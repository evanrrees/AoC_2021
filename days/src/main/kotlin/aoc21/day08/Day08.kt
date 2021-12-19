package aoc21.day08

import utils.split.mapSplit
import java.io.File


data class InputLine(val signal: List<Int>, val output: List<Int>) {
    constructor(signal: String, output: String): this(
        signal.split(" ").map(::encodeString), output.split(" ").map(::encodeString)
    )
    constructor(input: List<List<Int>>): this(input[0], input[1])
    constructor(input: Pair<List<Int>, List<Int>>): this(input.first, input.second)
}
operator fun <T> Array<T>.get(char: Char): T = this[char.lowercaseChar() - 'a']
operator fun <T> Array<T>.set(char: Char, value: T) = this.set(char.lowercaseChar() - 'a', value)

fun encodeString(string: String) = string.map { it - 'a' }.fold(0) { acc, it -> (1 shl it) + acc }

fun decodeDisplay(inputLine: InputLine) = decodeDisplay(inputLine.signal, inputLine.output)

fun decodeDisplay(signal: List<Int>, output: List<Int>): Int {

    val segments = Array(7) { 0 }
    val digits = Array(10) { 0 }
    val segmentCounts = (0..6).map { 1 shl it }.map { x -> signal.count { it and x == x } }

    // these segments can be uniquely identified by their frequency
    segments['e'] = 1 shl segmentCounts.withIndex().single { it.value == 4 }.index
    segments['b'] = 1 shl segmentCounts.withIndex().single { it.value == 6 }.index
    segments['f'] = 1 shl segmentCounts.withIndex().single { it.value == 9 }.index
    // these digits can be identified by number of segments
    digits[1] = signal.single { it.countOneBits() == 2 }
    digits[7] = signal.single { it.countOneBits() == 3 }
    digits[4] = signal.single { it.countOneBits() == 4 }
    digits[8] = signal.single { it.countOneBits() == 7 }

    // these digits and segments can be identified based on the first set of digits and segments
    segments['a'] = digits[7] xor digits[1]
    segments['c'] = digits[1] xor segments['f']
    digits[2] = signal.single { it and segments['f'] == 0 }
    digits[9] = digits[8] xor segments['e']
    digits[3] = digits[8] xor segments['b'] xor segments['e']

    // these digits and segments can be identified based on the first and second set of digits and segments
    segments['g'] = digits[9] xor digits[4] xor segments['a']
    segments['d'] = digits[3] xor digits[7] xor segments['g']
    digits[0] = digits[8] xor segments['d']
    digits[5] = digits[8] xor segments['c'] xor segments['e']
    digits[6] = digits[8] xor segments['c']

    return output.joinToString("") { "${digits.indexOf(it)}" }.toInt()

}

fun parseInput(inputFile: File) = inputFile.useLines { lines ->
    lines.mapSplit(" | ") { it.let { (a, b) -> InputLine(a, b) } }.toList()
}

fun part1(parsedInput: List<InputLine>) = arrayOf(2, 3, 4, 7).run {
    parsedInput.sumOf { it.output.map(Int::countOneBits).count(this::contains) }
}

fun part2(parsedInput: List<InputLine>) = parsedInput.sumOf(::decodeDisplay)


fun main() {
    val inputFile = File("src/main/resources/Day08.txt")
    val parsedInput = parseInput(inputFile)
    val result1 = part1(parsedInput)
    println(result1)
    val result2 = part2(parsedInput)
    println(result2)
}

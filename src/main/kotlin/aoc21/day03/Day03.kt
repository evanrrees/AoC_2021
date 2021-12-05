package aoc21.day03

import java.io.File

fun part1(numbers: List<List<Int>>, n: Int = numbers.first().size) = numbers
    .reduce { acc, ints -> acc.zip(ints, Int::plus) }.reversed()
    .mapIndexed { index, it -> if (it * 2 >= numbers.size) 1 shl index else 0 }.sum()
    .let { it * (it xor ((1 shl n)) - 1) }

tailrec fun selectBin(numbers: List<List<Int>>, o2: Boolean = true, pos: Int = 0): List<List<Int>> =
    if (numbers.size == 1) numbers
    else {
        val filterValue = if ((numbers.sumOf { it[pos] } * 2 >= numbers.size) xor o2) 0 else 1
        selectBin(numbers.filter { it[pos] == filterValue }, o2, pos + 1)
    }

fun part2(numbers: List<List<Int>>, n: Int = numbers.first().lastIndex) = sequenceOf(true, false)
    .map { selectBin(numbers, it).single().mapIndexed { index, i -> i shl (n - index) }.sum() }
    .reduce(Int::times)

fun main(args: Array<String>) {
    val bits = File(args[0]).readLines().map { it.map(Char::digitToInt) }
    println(part1(bits))
    println(part2(bits))
}
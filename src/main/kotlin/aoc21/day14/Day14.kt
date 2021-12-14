package aoc21.day14

import aoc21.utils.split.mapSplit
import java.io.File
import kotlin.system.measureTimeMillis

data class ParsedInput(val template: String, val rules: Map<String, String>)

operator fun <T> Set<T>.get(index: Int): T = elementAt(index)

//val missing = setOf("HH", "HV", "HP", "HF", "HK", "VH", "VB", "NV", "NK", "PN", "PP", "PF", "FF", "KB", "KV", "KS")
tailrec fun doInsertion(template: String, rules: Map<String, String>, n: Int): String {
    return if (n == 0) template else {
        val new = template.windowed(2).withIndex()
            .joinToString("") { (i, s) -> (rules[s] ?: s).drop(if (i > 0) 1 else 0) }
        doInsertion(new, rules, n - 1)
    }
}

fun part1(parsedInput: ParsedInput): Int {
    val rules = parsedInput.rules.mapValues { (k, v) -> "${k[0]}$v${k[1]}" }
    val result = doInsertion(parsedInput.template, rules, 10)
    val counts = result.groupingBy { it }.eachCount()
    return counts.maxOf { it.value } - counts.minOf { it.value }
}

fun part2(parsedInput: ParsedInput, rounds: Int = 40): Long {
    val chars = with (parsedInput) { (template.toSet() + rules.flatMap { (k, v) -> (k + v).toSet() }).toSet() }
        .toSortedSet()
    val rules = Array(chars.size) { Array(chars.size) { -1 } }
    val counts = Array(chars.size) { Array(chars.size) { 0L } }

    parsedInput.rules.entries.forEach { (k, v) ->
        k.map(chars::indexOf).let { (i, j) -> rules[i][j] = chars.indexOf(v.single()) }
    }
    parsedInput.template.windowed(2).forEach {
        it.map(chars::indexOf).let { (i, j) -> counts[i][j]++ }
    }

    repeat(rounds) {
        val newCounts = Array(chars.size) { Array(chars.size) { 0L } }
        counts.forEachIndexed { i, row ->
            row.forEachIndexed { j, x ->
                val insert = rules[i][j]
                newCounts[i][insert] += x
                newCounts[insert][j] += x
            }
        }
        newCounts.copyInto(counts)
    }
    return calc(counts, chars, parsedInput.template)
}

fun calc(counts: Array<Array<Long>>, chars: Set<Char>, template: String): Long {
    val ccc = Array(chars.size) { i -> (counts[i].sumOf { it } + counts.sumOf { it[i] }) }
    ccc[chars.indexOf(template.first())]++
    ccc[chars.indexOf(template.last())]++
    ccc.forEachIndexed { i, it -> ccc[i] = it / 2L + it % 2 }
    return ccc.maxOf { it } - ccc.minOf { it }
}

fun printFormat(counts: Array<Array<Long>>, chars: Set<Char>) {
    val width = counts.maxOf { row -> row.maxOf { it.toString().length } }
    chars.joinToString(" ", prefix = "    ") { "$it".padStart(width) }.let(::println)
    counts.withIndex().joinToString("\n", postfix = "\n") { (i, row) ->
        row.joinToString(" ", prefix="${chars.elementAt(i)} | ") { "$it".padStart(width) }
    }
        .let(::println)
}

fun parseInput(inputFile: File): ParsedInput {
    val (rawTemplate, rawRules) = inputFile.useLines { lines ->
        lines.filter(String::isNotBlank)
            .mapSplit(" -> ")
            .partition { it.size == 1 }
    }
    val template = rawTemplate.single().single()

    val rules = rawRules.associate { (a, b) -> a to b }
    return ParsedInput(template, rules)
}

fun main() {
    val inputFile = File("src/main/resources/Day14.txt")
    val parsedInput = parseInput(inputFile)
    val result1 = part2(parsedInput, 10)
    println(result1)
    measureTimeMillis {
        val result2 = part2(parsedInput)
        println(result2)
    }.let(System.err::println)
}

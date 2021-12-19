package aoc21.day02

import utils.split.forEachSplit
import java.io.File

fun part1(input: File): Int {
    var x = 0
    var y = 0
    input.useLines { lines ->
        lines.forEachSplit(" ") { (command, units) ->
            when (command) {
                "forward"   -> x += units.toInt()
                "up"        -> y -= units.toInt()
                "down"      -> y += units.toInt()
                else        -> throw Exception("Invalid command: $command")
            }
        }
    }
    return x * y
}

fun part2(input: File): Int {
    var horiz = 0
    var depth = 0
    var aim = 0
    input.useLines { lines ->
        lines.forEachSplit(" ") { (command, unitString) ->
            val units = unitString.toInt()
            when (command) {
                "down"      -> aim += units
                "up"        -> aim -= units
                "forward"   -> {
                    horiz += units
                    depth += aim * units
                }
                else        -> throw Exception("Invalid command: $command")
            }
        }
    }
    return horiz * depth
}

fun main(args: Array<String>) {


    val input = File(args[0])
    val result1 = part1(input)
    val result2 = part2(input)
    println(result1)
    println(result2)
}
package aoc21.day18

import aoc21.utils.ranges.expand
import aoc21.utils.timeit
import java.io.File

fun part1(parsedInput: List<SnailfishNumber>) = parsedInput.reduce { a, b -> a + b }.magnitude

fun part2(rawInput: List<String>) =
    rawInput.expand(rawInput) { (a, b) -> SnailfishNumber(a) + SnailfishNumber(b) }.maxOf { it.magnitude }

class SnailfishNumber(string: CharIterator? = null) {

    constructor(string: String): this(string.drop(1).iterator())

    var value: Int?                            = null
    var parent:    SnailfishNumber?            = null
    val children                               = mutableListOf<SnailfishNumber>()
    val left                             get() = children.firstOrNull()
    val right                            get() = children.elementAtOrNull(1)
    val depth:     Int                   get() = if (parent == null) 0 else 1 + parent!!.depth
    val traverse:  List<SnailfishNumber> get() = listOf(this) + children.flatMap { it.traverse }
    val regular                          get() = value != null
    val subregular                       get() = !regular && left?.regular ?: false && right?.regular ?: false
    val explodable                       get() = subregular && depth >= 4
    val splittable                       get() = regular && value!! > 9
    val magnitude: Int                   get() = if (regular) value!! else 3 * left!!.magnitude + 2 * right!!.magnitude

    init {
        if (string != null) {
            while (string.hasNext()) {
                val c = string.next()
                when {
                    c == '['    -> addChild(SnailfishNumber(string))
                    c.isDigit() -> addChild(SnailfishNumber().also { it.value = c.digitToInt() })
                    c == ']'    -> break
                }
            }
        }
    }

    fun addChild(child: SnailfishNumber): SnailfishNumber = apply { children += child.also { it.parent = this } }

    override fun equals(other: Any?) = this === other || (javaClass == other?.javaClass && "$this" == "$other")

    override fun hashCode() = "$this".hashCode()

    override fun toString() = value?.toString() ?: "[$left,$right]"

    operator fun plus(other: SnailfishNumber) = SnailfishNumber().addChild(this).addChild(other).reduce()

    fun reduce(): SnailfishNumber = apply {
        val numbers = traverse.withIndex()
        val exploded = numbers.firstOrNull { (_, sn) -> sn.explodable }
            ?.let { (i, sn) ->
                numbers.lastOrNull { it.index < i && it.value.regular }
                    ?.let { (_, regLeft) -> regLeft.value = regLeft.value!! + sn.left!!.value!! }
                numbers.firstOrNull { it.index > i + 2 && it.value.regular }
                    ?.let { (_, regRight) -> regRight.value = regRight.value!! + sn.right!!.value!! }
                sn.children.clear()
                sn.value = 0
            }
        if (exploded != null) reduce()
        val split = numbers.firstOrNull { (_, sn) -> sn.splittable }
            ?.let { (_, sn) ->
                val (left, right) = sn.value!!.let { it / 2 to it / 2 + it % 2 }
                sn.value = null
                sn.addChild(SnailfishNumber().apply { value = left })
                sn.addChild(SnailfishNumber().apply { value = right })
            }
        if (split != null) reduce()
    }
}

fun parseInput(inputFile: File) = inputFile.useLines { it.map(::SnailfishNumber).toList() }

fun main() {
    val inputFile = File("src/main/resources/Day18.txt")
    timeit("Part 1:") { part1(parseInput(inputFile)) }
    timeit("Part 2:") { part2(inputFile.readLines()) }
}

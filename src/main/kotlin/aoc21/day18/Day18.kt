package aoc21.day18

import aoc21.utils.ranges.expand
import aoc21.utils.timeit
import java.io.File

fun part1(parsedInput: List<SnailfishNumber>) = parsedInput.reduce { a, b -> a + b }.magnitude

fun part2(rawInput: List<String>) =
    rawInput.expand(rawInput) { (a, b) -> SnailfishNumber(a) + SnailfishNumber(b) }.maxOf { it.magnitude }

class SnailfishNumber(string: CharIterator? = null) {

    constructor(string: String): this(string.drop(1).iterator())

    var value:             Int?                        = null
    private var parent:    SnailfishNumber?            = null
    private val children                               = mutableListOf<SnailfishNumber>()
    private val left                             get() = children.firstOrNull()
    private val right                            get() = children.elementAtOrNull(1)
    private val depth:     Int                   get() = if (parent == null) 0 else 1 + parent!!.depth
    private val traverse:  List<SnailfishNumber> get() = listOf(this) + children.flatMap { it.traverse }
    private val regular                          get() = value != null
    private val explodable                       get() = left?.regular ?: false && right?.regular ?: false && depth >= 4
    private val splittable                       get() = regular && value!! > 9
    val magnitude:         Int
        get() = if (regular) value!! else 3 * left!!.magnitude + 2 * right!!.magnitude

    init {
        if (string != null)
            while (string.hasNext()) {
                val c = string.next()
                when {
                    c == '['    -> addChild(SnailfishNumber(string))
                    c.isDigit() -> addChild(SnailfishNumber().also { it.value = c.digitToInt() })
                    c == ']'    -> break
                }
            }
    }

    private fun addChild(child: SnailfishNumber) = apply { children += child.also { it.parent = this } }

    override fun equals(other: Any?) = this === other || (javaClass == other?.javaClass && "$this" == "$other")

    override fun hashCode() = "$this".hashCode()

    override fun toString() = value?.toString() ?: "[$left,$right]"

    operator fun plus(other: SnailfishNumber) = SnailfishNumber().addChild(this).addChild(other).reduce()

    fun reduce(): SnailfishNumber = apply {
        val numbers = traverse.withIndex()
        numbers.firstOrNull { (_, sn) -> sn.explodable }
            ?.let { (index, sn) ->
                numbers.lastOrNull { it.index < index && it.value.regular }
                    ?.let { (_, regLeft) -> regLeft.value = regLeft.value!! + sn.left!!.value!! }
                numbers.firstOrNull { it.index > index + 2 && it.value.regular }
                    ?.let { (_, regRight) -> regRight.value = regRight.value!! + sn.right!!.value!! }
                sn.children.clear()
                sn.value = 0
                reduce()
            }
        numbers.firstOrNull { (_, sn) -> sn.splittable }
            ?.let { (_, sn) ->
                val (left, right) = sn.value!!.let { it / 2 to it / 2 + it % 2 }
                sn.addChild(SnailfishNumber().apply { value = left })
                    .addChild(SnailfishNumber().apply { value = right })
                sn.value = null
                reduce()
            }
    }
}

fun parseInput(inputFile: File) = inputFile.useLines { it.map(::SnailfishNumber).toList() }

fun main() {
    val inputFile = File("src/main/resources/Day18.txt")
    timeit("Part 1:") { part1(parseInput(inputFile)) }
    timeit("Part 2:") { part2(inputFile.readLines()) }
}

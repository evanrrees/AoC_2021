package aoc21.day24

import org.tinylog.kotlin.Logger
import utils.maxOf
import utils.minOf
import java.io.File
import utils.timeit

/*
    inp a - Read an input value and write it to variable a.
    add a b - Add the value of a to the value of b, then store the result in variable a.
    mul a b - Multiply the value of a by the value of b, then store the result in variable a.
    div a b - Divide the value of a by the value of b, truncate the result to an integer, then store the result in variable a. (Here, "truncate" means to round the value toward zero.)
    mod a b - Divide the value of a by the value of b, then store the remainder in variable a. (This is also called the modulo operation.)
    eql a b - If the value of a and b are equal, then store the value 1 in variable a. Otherwise, store the value 0 in variable a.
 */

internal class SimpleProgram(private val constants: List<Triple<Int, Int, Int>>) {

    private fun simplifiedProgram(w: Int, z: Int, x5: Int, x6: Int, y16: Int): Int =
        if (((z / x5) % 26) + x6 != w) (z / x5) * 26 + w + y16 else z

    private fun simplifiedProgram(w: Int, z: Int, constants: Triple<Int, Int, Int>) =
        constants.run { simplifiedProgram(w, z, first, second, third) }

    fun run(n: Int, w: Int, z: Int) = simplifiedProgram(w, z, constants[n])
    fun run(vararg ints: Int): Int {
        var z = 0
        for ((index, w) in ints.withIndex()) {
            z = simplifiedProgram(w, z, constants[index])
        }
        return z
    }
}

internal class Program(instructions: List<InstructionInstance>) {
    val instructions = instructions.toList()
    val subprograms = instructions.chunked(18)
    fun constants(): List<Triple<Int, Int, Int>> {
        return subprograms.map { sub ->
            Triple(sub[4].arg2!!.toInt(), sub[5].arg2!!.toInt(), sub[15].arg2!!.toInt())
        }
    }

    fun simplify() = SimpleProgram(constants())

    constructor(file: File) : this(file.readLines().map(::InstructionInstance))

    fun runSubprogram(subprogram: Int, w: Int, z: Int): Int {
        val instance = ProgramInstance(w, z).apply {
            for (instruction in subprograms[subprogram])
                instruction(this)
        }
        return instance.z
    }

    fun run(vararg ints: Int) = ProgramInstance(input = ints).run()
    fun run(int: Int, vars: Map<String, Int>) = ProgramInstance(input = int, vars = vars.toMap()).run()

    fun split(): List<Program> {
        val iterator = instructions.iterator()
        val stack = mutableListOf(iterator.next())
        val result = mutableListOf<Program>()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next.instruction == Instruction.Inp) {
                result.add(Program(stack))
                stack.clear()
            }
            stack.add(next)
        }
        return result + Program(stack)
    }

    inner class ProgramInstance(
        vararg val input: Int,
        map: Map<String, Int> = emptyMap()
    ) {
        constructor(input: Int, vars: Map<String, Int>) : this(input, map = vars)
        constructor(w: Int, z: Int) : this(w, mapOf("z" to z, "w" to w))

        val vars: MutableMap<String, Int> = map.toMutableMap().withDefault { 0 }
        var w by vars
        var x by vars
        var y by vars
        var z by vars
        fun run() = apply {
            for ((subprogram, inp) in subprograms.zip(input.toList())) {
                w = inp
                for (instruction in subprogram) instruction(this)
            }
        }

        fun result() = vars.getValue(instructions.last().arg1)
        operator fun get(variable: String) = vars.getValue(variable)
        override fun toString() = "ProgramInstance(w=$w, x=$x, y=$y, z=$z)"
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ProgramInstance) return false
            if (z != other.z) return false
            return true
        }

        override fun hashCode() = z

    }

    internal enum class Instruction(
        val operand1Getter: ProgramInstance.(variable: String) -> Int? =
            { variable -> vars[variable] },
        val operand2Getter: ProgramInstance.(string: String?) -> Int =
            { string -> string!!.toIntOrNull() ?: vars.getValue(string) },
        val operation: (a: Int?, b: Int) -> Int
    ) {
        Inp({ null }, { w }, { _, b -> b }),
        Add(operation = { a, b -> (a ?: 0) + b }),
        Mul(operation = { a, b -> (a ?: 0) * b }),
        Div(operation = { a, b -> (a ?: 0) / b }),
        Mod(operation = { a, b -> (a ?: 0) % b }),
        Eql(operation = { a, b -> if (a == b) 1 else 0 });

        operator fun invoke(receiver: ProgramInstance, arg1: String, arg2: String?): ProgramInstance =
            receiver.apply { vars[arg1] = operation(operand1Getter(arg1), operand2Getter(arg2)) }

        companion object {
            operator fun invoke(string: String) = values().single { it.name.lowercase() == string }
        }

    }

    internal class InstructionInstance(val instruction: Instruction, val arg1: String, val arg2: String?) {
        constructor(strings: List<String>) : this(Instruction(strings[0]), strings[1], strings.getOrNull(2))
        constructor(string: String) : this(string.split(" "))

        operator fun invoke(receiver: ProgramInstance): ProgramInstance = instruction(receiver, arg1, arg2)
        override fun toString() = "${instruction.name.lowercase()} $arg1${arg2?.let { " $it" } ?: ""}"
    }

    override fun toString() = instructions.joinToString("\n")

}


internal fun monadSubprogram(w: Int, z0: Int, x5: Int, x6: Int, y16: Int): Int {
    var z = z0
    var x = 0                   // mul x 0
    x += z                      // add x z
    x %= 26                     // mod x 26
    z /= x5                     // div z 1
    x += x6                     // add x 13
    x = if (x == w) 1 else 0    // eql x w
    x = if (x == 0) 1 else 0    // eql x 0
    var y = 0                   // mul y 0
    y += 25                     // add y 25
    y *= x                      // mul y x
    y += 1                      // add y 1
    z *= y                      // mul z y
    y *= 0                      // mul y 0
    y += w                      // add y w
    y += y16                    // add y 3
    y *= x                      // mul y x
    z += y                      // add z y
    return z
}

internal fun solve(part1: Boolean = true, constants: List<Triple<Int, Int, Int>>): String {

    val comparator: (a: Pair<Int, Int>, b: Pair<Int, Int>) -> Pair<Int, Int> =
        if (part1) { { a, b -> maxOf(a, b, Pair<Int, Int>::second) } }
        else { { a, b -> minOf(a, b, Pair<Int, Int>::second) } }

    val paths = List(14) { mutableMapOf<Int, Pair<Int, Int>>() }

    for (n in 0 until 14) {
        val (x5, x6, y16) = constants[n]
        for (z0 in paths.getOrNull(n - 1)?.keys ?: listOf(0))
            for (w in 1..9) {
                val z = monadSubprogram(w, z0, x5, x6, y16)
//                if (z < 10_000_000) // very effective heuristic - reduces runtime by ~40x
                    paths[n].merge(z, z0 to w, comparator)
            }
    }

    val traceback = Array(13) { 0 }
    var prev = paths.last().getValue(0)
    for (n in 12 downTo 0) {
        traceback[n] = prev.second
        prev = paths[n].getValue(prev.first)
    }

    return traceback.joinToString("", "${prev.second}")
}

internal fun main() {
    val inputFile = File("days/src/main/resources/Day24.txt")
    val constants = Program(inputFile).constants()
    timeit("Part 1:") { solve(true, constants) }
    timeit("Part 2:") { solve(false, constants) }
}

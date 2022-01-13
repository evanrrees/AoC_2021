package aoc21.day24

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.tinylog.kotlin.Logger
import utils.maxOf
import utils.minOf
import utils.timeit
import java.io.File
import kotlin.random.Random

private val prog1File = File("src/test/resources/day24/prog1.txt")
private val prog2File = File("src/test/resources/day24/prog2.txt")
private val prog3File = File("src/test/resources/day24/prog3.txt")
private val progMainFile = File("/Users/err87/repos/evanrr/AoC_2021/days/src/main/resources/Day24.txt")

internal class Day24KtTest {

    object Main {
        @Test
        fun mainProgram() {
            val prog = Program(progMainFile)
            var input = IntArray(14) { 1 }
            var result = prog.run(*input)

            input = IntArray(14) { 9 }
            result = prog.run(*input)
            result
        }

        @Test
        fun parts() {
            val constants = Program(progMainFile).constants()
            val part1 = false

            val comparator: (a: Pair<Int, Int>, b: Pair<Int, Int>) -> Pair<Int, Int> =
                if (part1) { { a, b -> maxOf(a, b, Pair<Int, Int>::second) } }
                else { { a, b -> minOf(a, b, Pair<Int, Int>::second) } }

            val paths = List(14) { mutableMapOf<Int, Pair<Int, Int>>() }
            val runtime = Runtime.getRuntime()

            for (n in 0 until 14) {
                val (x5, x6, y16) = constants[n]
                for (z0 in paths.getOrNull(n - 1)?.keys ?: listOf(0))
                    for (w in 1..9) {
                        val z = monadSubprogram(w, z0, x5, x6, y16)
                        paths[n].merge(z, z0 to w, comparator)
                    }
                Logger.debug("Step %d: %,dMB}".format(n, runtime.run { (totalMemory() - freeMemory()) / 1_000_000 }))
            }

            val traceback = Array(13) { 0 }
            var prev = paths.last().getValue(0)
            for (n in 12 downTo 0) {
                traceback[n] = prev.second
                prev = paths[n].getValue(prev.first)
            }

            println(traceback.joinToString("", "${prev.second}"))
            println(paths.sumOf { it.size })
        }

        @Test
        fun simple() {
            val prog = Program(progMainFile)
            val simple = prog.simplify()
            val args = IntArray(14) { Random.nextInt(1, 9) }
            timeit("Full:") {
                prog.run(*args).z
            }
            timeit("Simple:") {
                simple.run(*args)
            }
            val result1 = prog.run(*args)
            val result2 = simple.run(*args)
            assertEquals(result1.z, result2)
        }

    }

    @Test
    fun simplify() {
        val main = Program(progMainFile)
        val programs = main.split()
        val args = programs.associateWith { prog ->
            prog.instructions.flatMap { inst ->
                "$inst".split(" ").drop(1).filter { it.singleOrNull() in 'w'..'z' }
            }.toSet()
        }
        args.values.distinctBy { it.size }
        val f = File("/Users/err87/repos/evanrr/AoC_2021/days/src/main/resources/day24/wide.tsv")
        f.bufferedWriter().use { writer ->
            programs.forEach { prog -> prog.instructions.joinToString("\t").let(writer::appendLine) }
        }
    }

    @Test
    fun unrelated() {
        fun <T> Collection<Iterable<T>>.forEachNested(operation: (List<T>) -> Unit) {
            fun _rec(i: Int = 0, args: List<T> = emptyList()) {
                if (i == size) operation(args)
                else for (item in elementAt(i)) _rec(i + 1, args + item)
            }
            _rec()
        }
    }

    object Part1 {

        val prog1 = Program(prog1File)
        val prog2 = Program(prog2File)
        val prog3 = Program(prog3File)

        class Prog1 {
            // program should negate number
            @Test
            fun `1`() {
                val expect = -1
                val actual = prog1.run(1).result()
                assertEquals(expect, actual)
            }

            @Test
            fun `2`() {
                val expect = 1
                val actual = prog1.run(-1).result()
                assertEquals(expect, actual)
            }

            @Test
            fun `3`() {
                val expect = 0
                val actual = prog1.run(0).result()
                assertEquals(expect, actual)
            }
        }

        class Prog2 {
            @Test
            fun `1`() {
                val expect = 1
                val actual = prog2.run(1, 3).result()
                assertEquals(expect, actual)
            }

            @Test
            fun `2`() {
                val expect = 1
                val actual = prog2.run(-3, -9).result()
                assertEquals(expect, actual)
            }

            @Test
            fun `3`() {
                val expect = 0
                val actual = prog2.run(0, 3).result()
                assertEquals(expect, actual)
            }

            @Test
            fun `4`() {
                val expect = 0
                val actual = prog2.run(3, 1).result()
                assertEquals(expect, actual)
            }

            @Test
            fun `5`() {
                val expect = 0
                val actual = prog2.run(3, -1).result()
                assertEquals(expect, actual)
            }
        }

        class Prog3 {
            @Test
            fun `1`() {
                val expect = 1
                val actual = prog3.run(1).z
                assertEquals(expect, actual)
            }

            @Test
            fun `2`() {
                val expect = 0
                val actual = prog3.run(2).z
                assertEquals(expect, actual)
            }

            @Test
            fun `3`() {
                val expect = 1
                val actual = prog3.run(3).z
                assertEquals(expect, actual)
            }

            @Test
            fun `4`() {
                val expect = 0
                val actual = prog3.run(4).z
                assertEquals(expect, actual)
            }

            @Test
            fun `5`() {
                val expect = 1
                val actual = prog3.run(5).z
                assertEquals(expect, actual)
            }
        }

        @Test
        fun split() {
            val program = Program(progMainFile)
            val subprograms = program.split()
            val expect = progMainFile.readLines()
            val actual = subprograms.flatMap { sub -> sub.instructions.map { "$it" } }
            assertEquals(expect, actual)
        }

        @Test
        fun traverseSub() {
            val main = Program(progMainFile)
//            val x = main.runSubprogram(0, 9, 0)
//            val zStack = List(main.subprograms.size) { mutableSetOf<Int>() }
            val zwStack = List(main.subprograms.size) { mutableMapOf<Int, Int>() }
            for (w in 1..9) {
                val result = main.runSubprogram(subprogram = 0, w = w, z = 0)
                zwStack.first().merge(result, w) { a, b -> maxOf(a, b) }
//                zStack.first().add(result)
            }
            for (n in 1..main.subprograms.lastIndex) {
                for (z in zwStack[n - 1]) {
                    for (w in 1..9) {
                        val result = main.runSubprogram(subprogram = n, w = w, z = z.key)
                        if (result < 10_000_000) {
                            zwStack[n].merge(result, w) { a, b -> maxOf(a, b) }
//                            zStack[n].add(result)
                        }
//                        zStack[n].add(main.runSubprogram(subprogram = n, w = w, z = z))
                    }
                }
            }
            zwStack.last()
            zwStack.last().size
        }

        @Test
        fun traverse3() {
            val main = Program(progMainFile)
            val programs = main.split()
            val states = mutableSetOf<Program.ProgramInstance>()
            for ((n, program) in programs.withIndex()) {
                val map = mutableMapOf<Int, Program.ProgramInstance>()
                if (n == 0) {
                    for (i in 9 downTo 1) {
                        val new = program.run(i)
                        map.merge(new.z, new) { a, b -> if (a.input.single() > b.input.single()) a else b }
                    }
                } else {
                    for (i in 9 downTo 1) {
                        for (prev in states) {
                            val new = program.run(i, prev.vars)
                            map.merge(new.z, new) { a, b -> if (a.input.single() > b.input.single()) a else b }
                        }
                    }
                }
                states.clear()
                states.addAll(map.values)
            }
            states.size
        }

        @Test
        fun traverse2() {
            val main = Program(progMainFile)
            val programs = main.split()
            val edges = mutableMapOf<Program, Map<Program.ProgramInstance, List<Int>>>()
            var stateCount = 0
            for ((index, program) in programs.withIndex()) {
                edges[program] = if (index == 0) {
                    (1..9).groupBy { program.run(it) }
                } else {
                    val previous = edges.getValue(programs[index - 1]).keys
                    val newMap = mutableMapOf<Program.ProgramInstance, MutableList<Int>>()
                    for (prev in previous) {
                        for (i in 1..9) {
                            val result = program.run(i, prev.vars)
                            newMap.getOrPut(result) { mutableListOf() } += i
                        }
                    }
                    newMap
                }
                    .also { stateCount += it.size }
            }
            edges.size
        }

        @Test
        fun traverse() {
            val prog = Program(progMainFile)
            val subprograms = prog.split()
            val subprogramIterator = subprograms.iterator()
            val states = mutableSetOf<Program.ProgramInstance>()
            while (subprogramIterator.hasNext()) {
                val subprogram = subprogramIterator.next()
                val newStates = if (states.isEmpty()) {
                    (1..9).map { subprogram.run(it) }
                } else {
                    states.flatMap { state -> (1..9).map { subprogram.run(it, vars = state.vars) } }
                }
                states.clear()
                states.addAll(newStates)
            }
            states.size
        }
    }

}

#!/usr/bin/env bash

# Sets up package structure and templates for a new day

set -eu

declare WORKDIR
WORKDIR=$(realpath "$(dirname "$0")")

if (( $# != 1 )); then
  echo "Usage: $0 DAY"
  exit 1
elif (( $1 < 1 || $1 > 25 )); then
  echo "DAY must by an integer in the range [1, 25]" > /dev/stderr
  echo "Usage: $0 DAY"
  exit 1
fi

declare DAY
printf -v DAY "%02s" "$1"

declare -a PACKAGES=( "$WORKDIR/src/"{main,test}"/kotlin/aoc21/day$DAY" )
declare -a INPUTS=( "$WORKDIR/src/"{main,test}"/resources/Day$DAY.txt" )
declare MAIN="$WORKDIR/src/main/kotlin/aoc21/day$DAY/Day$DAY.kt"
declare TEST="$WORKDIR/src/test/kotlin/aoc21/day$DAY/Day${DAY}KtTest.kt"

# make package directories
mkdir -p "${PACKAGES[@]}"

# touch input files
touch "${INPUTS[@]}"

# create main template
cat > "$MAIN" <<EOF
package aoc21.day$DAY

import java.io.File

fun part1(parsedInput: List<Int>): Int {
    TODO()
}

fun part2(parsedInput: List<Int>): Int {
    TODO()
}

fun parseInput(inputFile: File): List<Int> {
  TODO()
}

fun main() {
    val inputFile = File("src/main/resources/Day$DAY.txt")
    val parsedInput = parseInput(inputFile)
    val result1 = part1(parsedInput)
    println(result1)
    // val result2 = part2(parsedInput)
    // println(result2)
}
EOF

# create test template
cat > "$TEST" <<EOF
package aoc21.day$DAY

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class Day${DAY}KtTest {

    val inputFile = File("src/test/resources/Day$DAY.txt")
    val parsedInput = parseInput(inputFile)

    @Test
    fun part1() {
        val expect: Int = TODO()
        val actual = part1(parsedInput)
        assertEquals(expect, actual)
    }

    @Test
    fun part2() {
        val expect: Int = TODO()
        val actual = part2(parsedInput)
        assertEquals(expect, actual)
    }
}
EOF

git add "$MAIN" "$TEST" "${INPUTS[@]}"
package utils.io

import java.io.File

fun readIntGrid(inputFile: File) =
    inputFile.useLines { lines -> lines.map { it.map(Char::digitToInt).toTypedArray() }.toList() }.toTypedArray()

fun readLongGrid(inputFile: File) =
    inputFile.useLines { lines -> lines.map { it.map(Char::digitToInt).map(Int::toLong).toTypedArray() }.toList() }
        .toTypedArray()

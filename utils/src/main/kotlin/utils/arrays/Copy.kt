package utils.arrays


inline fun <reified T> Array<Array<T>>.deepCopyOf() = Array(size) { this[it].copyOf() }
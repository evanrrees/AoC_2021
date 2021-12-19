package utils

data class Progress(val n: Int) {
    var count = 1
    val width = "$n".length + 1
    fun report() {
        if (count % 10000 == 0)
        "\r(%-${width}d / %-${width}d)".format(count, n).let(::print)
        count++
        if (count > n) println()
    }
}
package utils.split

fun <R> CharSequence.split(
    vararg delimiters: String,
    ignoreCase: Boolean = false,
    limit: Int = 0,
    transform: (String) -> R
) =
    split(*delimiters, ignoreCase = ignoreCase, limit = limit).map(transform)
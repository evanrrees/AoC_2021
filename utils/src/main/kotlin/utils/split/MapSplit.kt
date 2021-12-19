package utils.split

fun Iterable<CharSequence>.mapSplit(
    vararg delimiters: String,
    ignoreCase: Boolean = false,
    limit: Int = 0
) =
    map { it.split(*delimiters, ignoreCase = ignoreCase, limit = limit) }


fun <R> Iterable<CharSequence>.mapSplit(
    vararg delimiters: String,
    ignoreCase: Boolean = false,
    limit: Int = 0,
    transform: (List<String>) -> R
) =
    mapSplit(*delimiters, ignoreCase = ignoreCase, limit = limit)
        .map(transform)

fun Iterable<CharSequence>.forEachSplit(
    vararg delimiters: String,
    ignoreCase: Boolean = false,
    limit: Int = 0,
    action: (List<String>) -> Unit
) =
    mapSplit(*delimiters, ignoreCase = ignoreCase, limit = limit)
        .forEach(action)

fun Iterable<CharSequence>.onEachSplit(
    vararg delimiters: String,
    ignoreCase: Boolean = false,
    limit: Int = 0,
    action: (List<String>) -> Unit
) =
    mapSplit(*delimiters, ignoreCase = ignoreCase, limit = limit)
        .onEach(action)

fun Sequence<CharSequence>.mapSplit(
    vararg delimiters: String,
    ignoreCase: Boolean = false,
    limit: Int = 0
) =
    map { it.split(*delimiters, ignoreCase = ignoreCase, limit = limit) }

fun <R> Sequence<CharSequence>.mapSplit(
    vararg delimiters: String,
    ignoreCase: Boolean = false,
    limit: Int = 0,
    transform: (List<String>) -> R
) =
    mapSplit(*delimiters, ignoreCase = ignoreCase, limit = limit)
        .map(transform)


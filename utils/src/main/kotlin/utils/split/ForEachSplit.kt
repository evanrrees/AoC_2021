package utils.split

fun Sequence<CharSequence>.forEachSplit(
    vararg delimiters: String,
    ignoreCase: Boolean = false,
    limit: Int = 0,
    action: (List<String>) -> Unit
) =
    mapSplit(*delimiters, ignoreCase = ignoreCase, limit = limit)
        .forEach(action)
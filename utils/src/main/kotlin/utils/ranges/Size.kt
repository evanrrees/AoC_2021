package utils.ranges

val CharRange.size: Int get()  = last - first + 1
val IntRange .size: Int get()  = last - first + 1
val LongRange.size: Long get() = last - first + 1
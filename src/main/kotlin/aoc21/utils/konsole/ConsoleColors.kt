package aoc21.utils.konsole

enum class Face(val value: Int) { STANDARD(0), BOLD(1), UNDERLINED(4) }
enum class Color(val value: Int?) {
    RESET(null), BLACK(30), RED(31), GREEN(32), YELLOW(33), BLUE(34), PURPLE(35), CYAN(36), WHITE(37)
}
enum class Intensity(val value: Int) { STANDARD(0), BRIGHT(60) }
enum class Plane(val value: Int) { FOREGROUND(0), BACKGROUND(10) }

class ElementBuilder {
    var face:       Face       = Face.STANDARD
    var color:      Color      = Color.WHITE
    var intensity:  Intensity  = Intensity.STANDARD
    var plane:      Plane      = Plane.FOREGROUND

    val bold:       Unit get() { face       = Face.BOLD         }
    val underline:  Unit get() { face       = Face.UNDERLINED   }
    val bright:     Unit get() { intensity  = Intensity.BRIGHT }
    val background: Unit get() { plane      = Plane.BACKGROUND  }

    val black:  Unit get() { color = Color.BLACK  }
    val red:    Unit get() { color = Color.RED    }
    val green:  Unit get() { color = Color.GREEN  }
    val yellow: Unit get() { color = Color.YELLOW }
    val blue:   Unit get() { color = Color.BLUE   }
    val purple: Unit get() { color = Color.PURPLE }
    val cyan:   Unit get() { color = Color.CYAN   }
    val white:  Unit get() { color = Color.WHITE  }

    fun build(): String {
        val sb = StringBuilder()
        sb.append('\u001b')
        sb.append("[")
        if (color.value == null) {
            sb.append(0)
        } else {
            if (intensity == Intensity.BRIGHT || plane == Plane.FOREGROUND) {
                sb.append(face.value)
                sb.append(";")
            }
            sb.append(color.value!! + plane.value + intensity.value)
        }
        sb.append("m")
        return sb.toString()
    }
}

fun String.format(block: ElementBuilder.() -> Unit) = ElementBuilder().apply(block).build().plus(this)

fun String.reset() = this + ElementBuilder().apply { color = Color.RESET }.build()

//fun reset(): String = ElementBuilder().apply { color = Color.RESET }.build()

//fun compose(block: ElementBuilder.() -> Unit): String {
//    val builder = ElementBuilder()
//    return builder.apply(block).build()
//}

//fun foo() {
//    val s = compose { bright; background; blue() }
//}
//
//enum class ConsoleColors2(val value: String) {
//    RESET("\u001b[0m"),
//    BLACK("\u001b[0;30m"),
//    RED("\u001b[0;31m"),
//    GREEN("\u001b[0;32m"),
//    YELLOW("\u001b[0;33m"),
//    BLUE("\u001b[0;34m"),
//    PURPLE("\u001b[0;35m"),
//    CYAN("\u001b[0;36m"),
//    WHITE("\u001b[0;37m"),
//    BLACK_BOLD("\u001b[1;30m"),
//    RED_BOLD("\u001b[1;31m"),
//    GREEN_BOLD("\u001b[1;32m"),
//    YELLOW_BOLD("\u001b[1;33m"),
//    BLUE_BOLD("\u001b[1;34m"),
//    PURPLE_BOLD("\u001b[1;35m"),
//    CYAN_BOLD("\u001b[1;36m"),
//    WHITE_BOLD("\u001b[1;37m"),
//    BLACK_UNDERLINED("\u001b[4;30m"),
//    RED_UNDERLINED("\u001b[4;31m"),
//    GREEN_UNDERLINED("\u001b[4;32m"),
//    YELLOW_UNDERLINED("\u001b[4;33m"),
//    BLUE_UNDERLINED("\u001b[4;34m"),
//    PURPLE_UNDERLINED("\u001b[4;35m"),
//    CYAN_UNDERLINED("\u001b[4;36m"),
//    WHITE_UNDERLINED("\u001b[4;37m"),
//    BLACK_BACKGROUND("\u001b[40m"),
//    RED_BACKGROUND("\u001b[41m"),
//    GREEN_BACKGROUND("\u001b[42m"),
//    YELLOW_BACKGROUND("\u001b[43m"),
//    BLUE_BACKGROUND("\u001b[44m"),
//    PURPLE_BACKGROUND("\u001b[45m"),
//    CYAN_BACKGROUND("\u001b[46m"),
//    WHITE_BACKGROUND("\u001b[47m"),
//    BLACK_BRIGHT("\u001b[0;90m"),
//    RED_BRIGHT("\u001b[0;91m"),
//    GREEN_BRIGHT("\u001b[0;92m"),
//    YELLOW_BRIGHT("\u001b[0;93m"),
//    BLUE_BRIGHT("\u001b[0;94m"),
//    PURPLE_BRIGHT("\u001b[0;95m"),
//    CYAN_BRIGHT("\u001b[0;96m"),
//    WHITE_BRIGHT("\u001b[0;97m"),
//    BLACK_BOLD_BRIGHT("\u001b[1;90m"),
//    RED_BOLD_BRIGHT("\u001b[1;91m"),
//    GREEN_BOLD_BRIGHT("\u001b[1;92m"),
//    YELLOW_BOLD_BRIGHT("\u001b[1;93m"),
//    BLUE_BOLD_BRIGHT("\u001b[1;94m"),
//    PURPLE_BOLD_BRIGHT("\u001b[1;95m"),
//    CYAN_BOLD_BRIGHT("\u001b[1;96m"),
//    WHITE_BOLD_BRIGHT("\u001b[1;97m"),
//    BLACK_BACKGROUND_BRIGHT("\u001b[0;100m"),
//    RED_BACKGROUND_BRIGHT("\u001b[0;101m"),
//    GREEN_BACKGROUND_BRIGHT("\u001b[0;102m"),
//    YELLOW_BACKGROUND_BRIGHT("\u001b[0;103m"),
//    BLUE_BACKGROUND_BRIGHT("\u001b[0;104m"),
//    PURPLE_BACKGROUND_BRIGHT("\u001b[0;105m"),
//    CYAN_BACKGROUND_BRIGHT("\u001b[0;106m"),
//    WHITE_BACKGROUND_BRIGHT("\u001b[0;107m");
//
//    override fun toString(): String {
//        return value
//    }
//}
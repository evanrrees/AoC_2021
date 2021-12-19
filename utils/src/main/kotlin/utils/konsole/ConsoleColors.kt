package utils.konsole

enum class Face(val value: Int) { STANDARD(0), BOLD(1), UNDERLINED(4) }
enum class Color(val value: Int?) {
    RESET(null), BLACK(30), RED(31), GREEN(32), YELLOW(33), BLUE(34), PURPLE(35), CYAN(36), WHITE(37), DEFAULT(39)
}
enum class Intensity(val value: Int) { STANDARD(0), BRIGHT(60) }
enum class Plane(val value: Int) { FOREGROUND(0), BACKGROUND(10) }

class ElementBuilder {
    var face: Face = Face.STANDARD
    var color: Color = Color.WHITE
    var intensity: Intensity = Intensity.STANDARD
    var plane: Plane = Plane.FOREGROUND

    val bold:       Unit get() { face       = Face.BOLD }
    val underline:  Unit get() { face       = Face.UNDERLINED }
    val bright:     Unit get() { intensity  = Intensity.BRIGHT }
    val background: Unit get() { plane      = Plane.BACKGROUND }

    val black:  Unit get() { color = Color.BLACK }
    val red:    Unit get() { color = Color.RED }
    val green:  Unit get() { color = Color.GREEN }
    val yellow: Unit get() { color = Color.YELLOW }
    val blue:   Unit get() { color = Color.BLUE }
    val purple: Unit get() { color = Color.PURPLE }
    val cyan:   Unit get() { color = Color.CYAN }
    val white:  Unit get() { color = Color.WHITE }

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

//fun String.reset() = this + ElementBuilder().apply { color = Color.RESET }.build()

const val ASCII_ESCAPE = '\u001b'

fun String.bold()       = "$ASCII_ESCAPE[1m$this"
fun String.dim()        = "$ASCII_ESCAPE[2m$this"
fun String.underline()  = "$ASCII_ESCAPE[4m$this"
fun String.blink()      = "$ASCII_ESCAPE[5m$this"
fun String.invert()     = "$ASCII_ESCAPE[7m$this"
fun String.hide()       = "$ASCII_ESCAPE[8m$this"
fun String.defaultForeground() = "$ASCII_ESCAPE[39m$this"
fun String.defaultBackground() = "$ASCII_ESCAPE[49m$this"
fun String.reset()          = "$this$ASCII_ESCAPE[0m"
fun String.resetBold()      = "$this$ASCII_ESCAPE[21m"
fun String.resetDim()       = "$this$ASCII_ESCAPE[22m"
fun String.resetUnderline() = "$this$ASCII_ESCAPE[24m"
fun String.resetBlink()     = "$this$ASCII_ESCAPE[25m"
fun String.resetInvert()    = "$this$ASCII_ESCAPE[27m"
fun String.resetHide()      = "$this$ASCII_ESCAPE[28m"

class ANSI(var string: String = "") {
    fun bold()              { string += "$ASCII_ESCAPE[1m" }
    fun dim()               { string += "$ASCII_ESCAPE[2m" }
    fun underline()         { string += "$ASCII_ESCAPE[4m" }
    fun blink()             { string += "$ASCII_ESCAPE[5m" }
    fun invert()            { string += "$ASCII_ESCAPE[7m" }
    fun hide()              { string += "$ASCII_ESCAPE[8m" }
    fun defaultForeground() { string += "$ASCII_ESCAPE[39m" }
    fun defaultBackground() { string += "$ASCII_ESCAPE[49m" }

    fun reset()          { string += "$ASCII_ESCAPE[0m" }
    fun resetBold()      { string += "$ASCII_ESCAPE[21m" }
    fun resetDim()       { string += "$ASCII_ESCAPE[22m" }
    fun resetUnderline() { string += "$ASCII_ESCAPE[24m" }
    fun resetBlink()     { string += "$ASCII_ESCAPE[25m" }
    fun resetInvert()    { string += "$ASCII_ESCAPE[27m" }
    fun resetHide()      { string += "$ASCII_ESCAPE[28m" }
    override fun toString() = string
}

fun String.ansi(block: ANSI.() -> Unit) = ANSI(this).apply(block)

val x = "".ansi {  }
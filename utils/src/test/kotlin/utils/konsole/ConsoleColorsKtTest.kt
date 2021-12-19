package utils.konsole

import org.junit.jupiter.api.Test

internal class ConsoleColorsKtTest {
    @Test
    fun foo() {
//        val string = "Foo"
        println("bold".bold())
        println("dim".dim())
        println("underlined".underline())
        println("blink".blink())
        println("inverted".invert())
        println("bold_underlined".bold().underline())
        println("hidden".hide())
        println("bold_underlined_invert".bold().underline().invert())
    }
}
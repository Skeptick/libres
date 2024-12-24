@file:Suppress("unused")

package io.github.skeptick.libres.strings

private val RegularFormatRegex = "%(\\d)\\$[s]".toRegex()

public class VoidFormattedString(private val value: String) {
    public fun format(vararg args: String): String = formatString(value, args)
}

public fun formatString(value: String, args: Array<out String>): String {
    return RegularFormatRegex.replace(value) { matchResult ->
        args[matchResult.groupValues[1].toInt() - 1]
    }
}
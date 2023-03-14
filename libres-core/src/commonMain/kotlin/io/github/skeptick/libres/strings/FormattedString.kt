@file:Suppress("unused")

package io.github.skeptick.libres.strings

private val RegularFormatRegex = "%(\\d)\\$[s]".toRegex()

class VoidFormattedString(private val value: String) {
    fun format(vararg args: String) = formatString(value, args)
}

fun formatString(value: String, args: Array<out String>): String {
    return RegularFormatRegex.replace(value) { matchResult ->
        args[matchResult.groupValues[1].toInt() - 1]
    }
}
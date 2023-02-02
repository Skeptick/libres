@file:Suppress("NOTHING_TO_INLINE", "PrivatePropertyName")

package io.github.skeptick.libres.plugin.strings

private val NameRegex = "^[a-zA-Z][a-zA-Z0-9_]*$".toRegex()

private val JavaFormatRegex = "(^|[^%])%([a-zA-Z]|[0-9]*\\$)".toRegex()

private val TemplateFormatRegex = "\\$\\{([a-zA-Z][a-zA-Z0-9_]*)}".toRegex()

internal inline fun String.hasJavaArguments() = contains(JavaFormatRegex)

internal inline fun String.isValidName() = isNotBlank() && matches(NameRegex)

internal inline fun String.unescapeXml() = replace("\\n", "\n")

internal fun String.snakeCaseToCamelCase(startWithLower: Boolean = false) =
    split('_').joinToString("") { it.capitalizeUS() }.let { string ->
        if (startWithLower) string.decapitalizeUS() else string
    }

internal fun String.extractInterpolationParametersNames() =
    TemplateFormatRegex.findAll(this).toList().map { result ->
        result.groupValues[1]
    }

internal fun String.replaceNamedArguments(formatter: (index: Int, name: String) -> String): String {
    var index = 1
    return replace(TemplateFormatRegex) { formatter(index++, it.groupValues[1]) }
}

internal fun String.capitalizeUS() = replaceFirstChar { it.titlecase() }

internal fun String.decapitalizeUS() = replaceFirstChar { it.lowercase() }
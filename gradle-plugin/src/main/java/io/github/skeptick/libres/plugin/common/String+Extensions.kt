package io.github.skeptick.libres.plugin.common

private val TemplateFormatRegex = Regex(pattern = "\\$\\{([a-zA-Z][a-zA-Z0-9_]*)}")

internal fun String.capitalize() = replaceFirstChar { it.titlecase() }

internal fun String.decapitalize() = replaceFirstChar { it.lowercase() }

internal fun String.snakeCaseToCamelCase(startWithLower: Boolean = false) =
    split('_').joinToString(separator = "", transform = String::capitalize).let { string ->
        if (startWithLower) string.decapitalize() else string
    }

internal fun String.extractInterpolationParameters() =
    TemplateFormatRegex.findAll(this).toList().map { result ->
        result.groupValues[1]
    }

internal fun String.replaceTemplateParameters(formatter: (index: Int, name: String) -> String): String {
    var index = 1
    return replace(TemplateFormatRegex) { formatter(index++, it.groupValues[1]) }
}
package io.github.skeptick.libres.plugin.strings.models

internal sealed interface TextResource {
    val name: String
    val parameters: Set<String>
}
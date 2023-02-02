package io.github.skeptick.libres.plugin.strings.models

import io.github.skeptick.libres.plugin.strings.extractInterpolationParametersNames

data class StringResource(
    override val name: String,
    val value: String
) : TextResource {

    override val parameters: Set<String> by lazy(LazyThreadSafetyMode.NONE) {
        value.extractInterpolationParametersNames().toSet()
    }

}
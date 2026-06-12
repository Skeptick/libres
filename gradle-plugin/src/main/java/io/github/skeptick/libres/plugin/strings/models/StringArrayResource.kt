package io.github.skeptick.libres.plugin.strings.models

import io.github.skeptick.libres.plugin.strings.extractInterpolationParametersNames

data class StringArrayResource(
    override val name: String,
    val items: List<String>
) : TextResource {
    override val parameters: Set<String> by lazy(LazyThreadSafetyMode.NONE) {
        items.flatMap { it.extractInterpolationParametersNames() }.toSet()
    }

}

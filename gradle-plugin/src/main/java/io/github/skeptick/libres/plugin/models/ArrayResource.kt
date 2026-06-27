package io.github.skeptick.libres.plugin.models

import io.github.skeptick.libres.plugin.common.extractInterpolationParameters

internal class ArrayResource(
    override val name: String,
    baseItems: List<String>,
    localizedItems: Map<LocaleTag, List<String>>
) : TextResource {

    override val parameters: Set<String> = baseItems.flatMap { it.extractInterpolationParameters() }.toSet()

    val localizedItems: Map<LocaleTag, List<String>> = localizedItems.mapValues { (locale, items) ->
        items.map { value ->
            replaceParametersToJavaSpecifiers(value, locale)
        }
    }

}
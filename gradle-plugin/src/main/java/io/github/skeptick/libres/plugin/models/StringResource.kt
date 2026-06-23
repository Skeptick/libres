package io.github.skeptick.libres.plugin.models

import io.github.skeptick.libres.plugin.common.extractInterpolationParameters

internal class StringResource(
    override val name: String,
    baseValue: String,
    localizedValues: Map<LocaleTag, String>
) : TextResource {

    override val parameters: Set<String> = baseValue.extractInterpolationParameters().toSet()

    val localizedValues: Map<LocaleTag, String> = localizedValues.mapValues { (locale, value) ->
        replaceParametersToJavaSpecifiers(value, locale)
    }

}
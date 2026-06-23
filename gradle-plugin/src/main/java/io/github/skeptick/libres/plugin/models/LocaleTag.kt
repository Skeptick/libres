package io.github.skeptick.libres.plugin.models

import io.github.skeptick.libres.plugin.parsing.LocaleTagNotFoundException
import io.github.skeptick.libres.plugin.common.capitalize
import java.io.File

private val LocaleTagRegex = Regex(
    pattern = "^([A-Za-z]{2,3})(?:-([A-Za-z]{4}))?(?:-([A-Za-z]{2}))?$"
)

internal data class LocaleTag(
    val language: String,
    val script: String?,
    val region: String?,
) : Comparable<LocaleTag> {

    val value: String = listOfNotNull(language, script, region).joinToString("-")

    val objectNameSuffix: String = listOfNotNull(language, script, region).joinToString("") { it.capitalize() }

    override fun compareTo(other: LocaleTag): Int = value.compareTo(other.value)

    override fun toString(): String = value

}

internal fun LocaleTag(value: String): LocaleTag {
    val (language, script, region) = LocaleTagRegex.matchEntire(value)?.destructured ?: throw InvalidLocaleTagException(value)
    return LocaleTag(
        language = language.lowercase(),
        script = script.takeIf(String::isNotEmpty)?.lowercase()?.capitalize(),
        region = region.takeIf(String::isNotEmpty)?.uppercase()
    )
}

internal fun File.parseLocaleTag(): LocaleTag {
    val localeTag = nameWithoutExtension.substringAfterLast("_", missingDelimiterValue = "")
    return if (localeTag.isEmpty()) throw LocaleTagNotFoundException(name) else LocaleTag(localeTag)
}

internal class InvalidLocaleTagException(tag: String) : Exception(
    "Invalid locale tag '$tag'. Use BCP 47 language[-Script][-REGION] tag, for example: 'en', 'en-US' or 'zh-Hans-CN'."
)
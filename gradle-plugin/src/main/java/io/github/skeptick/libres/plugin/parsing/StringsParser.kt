package io.github.skeptick.libres.plugin.parsing

import io.github.skeptick.libres.plugin.common.allAre
import io.github.skeptick.libres.plugin.models.LocaleTag
import io.github.skeptick.libres.plugin.models.PluralsResource
import io.github.skeptick.libres.plugin.models.PluralsResource.Companion.quantityBySerialName
import io.github.skeptick.libres.plugin.models.StringResource
import io.github.skeptick.libres.plugin.models.TextResource
import io.github.skeptick.libres.plugin.models.parseLocaleTag
import io.github.skeptick.libres.plugin.parsing.StringsXmlItem.PluralsItem
import io.github.skeptick.libres.plugin.parsing.StringsXmlItem.StringItem
import nl.adaptivity.xmlutil.serialization.XML
import java.io.File

private val JavaSpecifiersRegex = Regex(pattern = """%(?:[A-Za-z]|\d+\$[A-Za-z])""")

private val NameRegex = Regex(pattern = "^[a-zA-Z][a-zA-Z0-9_]*$")

internal fun parseStrings(inputFiles: Set<File>, baseLocaleTag: LocaleTag): List<TextResource> {
    return parseXmlStrings(inputFiles).map { (name, itemsByLocale) ->
        when {
            itemsByLocale.allAre<LocaleTag, StringItem>() -> itemsByLocale.buildStringResource(name, baseLocaleTag)
            itemsByLocale.allAre<LocaleTag, PluralsItem>() -> itemsByLocale.buildPluralsResource(name, baseLocaleTag)
            else -> throw DifferentStringResourceTypesException(name)
        }
    }
}

private fun parseXmlStrings(inputFiles: Set<File>): Map<String, Map<LocaleTag, StringsXmlItem>> {
    return buildMap<String, MutableMap<LocaleTag, StringsXmlItem>> {
        inputFiles.forEach { file ->
            val localeTag = file.parseLocaleTag()
            val xmlItems = XML.v1.decodeFromString(StringsXml.serializer(), file.readText()).items
            xmlItems.forEach { xmlItem ->
                val resourceMap = getOrPut(xmlItem.name, ::mutableMapOf)
                when {
                    !xmlItem.name.matches(NameRegex) -> throw InvalidStringResourceNameException(xmlItem.name)
                    resourceMap[localeTag] != null -> throw StringResourceNameClashException(localeTag, xmlItem.name)
                    else -> resourceMap[localeTag] = xmlItem
                }
            }
        }
    }
}

private fun Map<LocaleTag, StringItem>.buildStringResource(name: String, baseLocaleTag: LocaleTag): StringResource {
    requireBaseLocale(name, baseLocaleTag)
    requireNoJavaSpecifiers(name)

    val localizedValues = mapValues { (_, item) -> item.value }
    return StringResource(
        name = name,
        baseValue = localizedValues.getValue(baseLocaleTag),
        localizedValues = mapValues { (_, stringItem) -> stringItem.value }
    )
}

private fun Map<LocaleTag, PluralsItem>.buildPluralsResource(name: String, baseLocaleTag: LocaleTag): PluralsResource {
    requireBaseLocale(name, baseLocaleTag)
    requireNoJavaSpecifiers(name)
    requireNotEmptyPluralItems(name)

    val localizedItems = mapValues { (localeTag, item) -> item.toPluralsResourceItems(name, localeTag) }
    return PluralsResource(
        name = name,
        baseItems = localizedItems.getValue(baseLocaleTag),
        localizedItems = localizedItems
    )
}

private fun <T> Map<LocaleTag, T>.requireBaseLocale(name: String, baseLocaleTag: LocaleTag) {
    if (baseLocaleTag !in this) {
        throw BaseStringResourcesNotFoundException(name, baseLocaleTag, keys.toList())
    }
}

private fun <T : StringsXmlItem> Map<LocaleTag, T>.requireNoJavaSpecifiers(name: String) {
    val (localeTag) = entries.firstOrNull { (_, item) -> item.hasJavaSpecifiers() } ?: return
    throw StringResourceInvalidFormatException(localeTag, name)
}

private fun Map<LocaleTag, PluralsItem>.requireNotEmptyPluralItems(name: String) {
    val (localeTag) = entries.firstOrNull { (_, item) -> item.items.isEmpty() } ?: return
    throw PluralStringWithoutQuantityException(localeTag, name)
}

private fun PluralsItem.toPluralsResourceItems(name: String, localeTag: LocaleTag): List<PluralsResource.Item> {
    return items.map {
        PluralsResource.Item(
            quantity = quantityBySerialName[it.quantity] ?: throw InvalidPluralQuantityException(localeTag, name),
            value = it.value
        )
    }
}

private fun StringsXmlItem.hasJavaSpecifiers(): Boolean {
    return when (this) {
        is StringItem -> value.contains(JavaSpecifiersRegex)
        is PluralsItem -> items.any { it.value.contains(JavaSpecifiersRegex) }
    }
}
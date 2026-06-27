package io.github.skeptick.libres.plugin.parsing

import io.github.skeptick.libres.plugin.common.allAre
import io.github.skeptick.libres.plugin.common.extractTemplateParameters
import io.github.skeptick.libres.plugin.common.isValidPropertyName
import io.github.skeptick.libres.plugin.models.ArrayResource
import io.github.skeptick.libres.plugin.models.LocaleTag
import io.github.skeptick.libres.plugin.models.PluralsResource
import io.github.skeptick.libres.plugin.models.PluralsResource.Companion.quantityBySerialName
import io.github.skeptick.libres.plugin.models.StringResource
import io.github.skeptick.libres.plugin.models.TextResource
import io.github.skeptick.libres.plugin.models.parseLocaleTag
import io.github.skeptick.libres.plugin.parsing.StringsXmlItem.ArrayItem
import io.github.skeptick.libres.plugin.parsing.StringsXmlItem.PluralsItem
import io.github.skeptick.libres.plugin.parsing.StringsXmlItem.StringItem
import nl.adaptivity.xmlutil.serialization.XML
import java.io.File

private val JavaSpecifiersRegex = Regex(pattern = """%(?:[A-Za-z]|\d+\$[A-Za-z])""")

internal fun parseStrings(inputFiles: Set<File>, baseLocaleTag: LocaleTag): List<TextResource> {
    return parseXmlStrings(inputFiles).map { (name, itemsByLocale) ->
        when {
            itemsByLocale.allAre<LocaleTag, StringItem>() -> itemsByLocale.buildStringResource(name, baseLocaleTag)
            itemsByLocale.allAre<LocaleTag, PluralsItem>() -> itemsByLocale.buildPluralsResource(name, baseLocaleTag)
            itemsByLocale.allAre<LocaleTag, ArrayItem>() -> itemsByLocale.buildArrayResource(name, baseLocaleTag)
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
                    !xmlItem.name.isValidPropertyName() -> throw InvalidStringResourceNameException(xmlItem.name)
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
    requireValidTemplateParameters(name, baseLocaleTag)

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
    requireValidTemplateParameters(name, baseLocaleTag)

    val localizedItems = mapValues { (localeTag, item) -> item.toPluralsResourceItems(name, localeTag) }
    return PluralsResource(
        name = name,
        baseItems = localizedItems.getValue(baseLocaleTag),
        localizedItems = localizedItems
    )
}

private fun Map<LocaleTag, ArrayItem>.buildArrayResource(name: String, baseLocaleTag: LocaleTag): ArrayResource {
    requireBaseLocale(name, baseLocaleTag)
    requireNoJavaSpecifiers(name)
    requireValidTemplateParameters(name, baseLocaleTag)

    val localizedItems = mapValues { (_, item) -> item.items.map(ArrayItem.Item::value) }
    return ArrayResource(
        name = name,
        baseItems = localizedItems.getValue(baseLocaleTag),
        localizedItems = localizedItems
    )
}

private fun Map<LocaleTag, *>.requireBaseLocale(name: String, baseLocaleTag: LocaleTag) {
    if (baseLocaleTag in this) return
    throw BaseStringResourcesNotFoundException(
        resourceName = name,
        baseLocaleTag = baseLocaleTag,
        existLocaleTags = keys.toList()
    )
}

private fun <T : StringsXmlItem> Map<LocaleTag, T>.requireNoJavaSpecifiers(name: String) {
    val (localeTag) = entries.firstOrNull { (_, item) -> item.hasJavaSpecifiers() } ?: return
    throw StringResourceInvalidFormatException(
        localeTag = localeTag,
        resourceName = name
    )
}

private fun Map<LocaleTag, PluralsItem>.requireNotEmptyPluralItems(name: String) {
    val (localeTag) = entries.firstOrNull { (_, item) -> item.items.isEmpty() } ?: return
    throw PluralStringWithoutQuantityException(
        localeTag = localeTag,
        resourceName = name
    )
}

private fun <T : StringsXmlItem> Map<LocaleTag, T>.requireValidTemplateParameters(name: String, baseLocaleTag: LocaleTag) {
    val invalidParameters = getValue(baseLocaleTag).extractInvalidTemplateParameters().takeIf(Set<*>::isNotEmpty) ?: return
    throw InvalidTemplateParameterNameException(
        localeTag = baseLocaleTag,
        resourceName = name,
        invalidParameters = invalidParameters
    )
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
        is ArrayItem -> items.any { it.value.contains(JavaSpecifiersRegex) }
    }
}

private fun StringsXmlItem.extractInvalidTemplateParameters(): Set<String> {
    return when (this) {
        is StringItem -> value.extractTemplateParameters().filterNot(String::isValidPropertyName).toSet()
        is PluralsItem -> items.flatMap { it.value.extractTemplateParameters().filterNot(String::isValidPropertyName) }.toSet()
        is ArrayItem -> items.flatMap { it.value.extractTemplateParameters().filterNot(String::isValidPropertyName) }.toSet()
    }
}
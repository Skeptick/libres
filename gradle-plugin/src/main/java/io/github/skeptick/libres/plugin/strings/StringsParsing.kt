package io.github.skeptick.libres.plugin.strings

import io.github.skeptick.libres.plugin.common.extensions.findDuplicates
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import io.github.skeptick.libres.plugin.strings.models.*
import java.io.File

private typealias Resources = Map<LanguageCode, List<TextResource>>

private val xmlMapper = XmlMapper()

internal fun parseStringResources(inputFiles: Set<File>, baseLocaleLanguageCode: LanguageCode): Resources {
    val filesByLocale = inputFiles.groupBy { it.name.substringAfterLast("_").substringBefore(".") }
    if (filesByLocale[baseLocaleLanguageCode].isNullOrEmpty()) throw BaseStringResourcesNotFoundException()

    val resources = filesByLocale.mapValues { (_, files) -> files.map(xmlMapper::readTree).flatMap(JsonNode::parseResources) }
    resources.validateNames(baseLocaleLanguageCode)
    resources.validateDuplicatesAndArguments()
    return resources
}

private fun Resources.validateNames(baseLocaleLanguageCode: LanguageCode) {
    val baseResources = get(baseLocaleLanguageCode) ?: throw BaseStringResourcesNotFoundException()
    val baseNames = baseResources.map(TextResource::name)
    if (!baseNames.all(String::isValidName)) throw UnavailableNameException(baseNames.first { !it.isValidName() })
}

private fun Resources.validateDuplicatesAndArguments() {
    forEach { (languageCode, localizedResources) ->
        val names = localizedResources.map(TextResource::name)
        val stringValues = localizedResources.filterIsInstance<StringResource>().map(StringResource::value)
        val pluralValues = localizedResources.filterIsInstance<PluralsResource>().flatMap { it.items.map(PluralsResource.Item::value) }
        names.findDuplicates().let { if (it.isNotEmpty()) throw NamesClashException(languageCode, it) }
        stringValues.find(String::hasJavaArguments)?.let { throw UnavailableFormatException(languageCode, it) }
        pluralValues.find(String::hasJavaArguments)?.let { throw UnavailableFormatException(languageCode, it) }
    }
}

class UnavailableNameException internal constructor(name: String) : Exception(
    "Name '$name' isn't available. Please use only Latin letters, numbers and underscore."
)

class NamesClashException internal constructor(languageCode: LanguageCode, names: Set<String>) : Exception(
    "Found strings with the identical names: $names. Locale: '$languageCode'."
)

class UnavailableFormatException internal constructor(languageCode: LanguageCode, stringName: String) : Exception(
    "Don't use java style formatting. Use \${template_like}. Locale: '$languageCode'. String: '$stringName'."
)

class BaseStringResourcesNotFoundException internal constructor() : Exception(
    "Couldn't find resource files for base localization."
)

class ParameterNotFoundException internal constructor(languageCode: LanguageCode, stringName: String, parameterName: String) : Exception(
    "Localized strings cannot have parameters missing in base localization. Locale: '$languageCode'. String: '$stringName'. Parameter: '$parameterName'."
)

class PluralStringWithoutQuantityException internal constructor(stringName: String) : Exception(
    "Plural string doesn't contain any quantity. String: $stringName"
)

class InvalidPluralQuantityException internal constructor(quantity: String) : Exception(
    "invalid quantity in plural string: '$quantity'."
)

private fun JsonNode.parseResources(): List<TextResource> {
    return this["string"]?.parseAsArray(JsonNode::parseString).orEmpty() +
            this["plurals"]?.parseAsArray(JsonNode::parsePlural).orEmpty()
}

private fun JsonNode.parseString(): StringResource {
    return StringResource(
        name = this["name"].asText(),
        value = this[""]?.asText() ?: ""
    )
}

private fun JsonNode.parsePlural(): PluralsResource {
    return PluralsResource(
        name = this["name"].asText(),
        items = this["item"]?.parseAsArray(JsonNode::parsePluralItem) ?: throw PluralStringWithoutQuantityException(this["name"].asText())
    )
}

private fun JsonNode.parsePluralItem(): PluralsResource.Item {
    return PluralsResource.Item(
        quantity = PluralsResource.quantityBySerialName[this["quantity"].asText()] ?: throw InvalidPluralQuantityException(this["quantity"].asText()),
        value = this[""]?.asText() ?: ""
    )
}

private inline fun <T> JsonNode.parseAsArray(block: (JsonNode) -> T): List<T> {
    return if (isArray) map(block) else listOf(block(this))
}
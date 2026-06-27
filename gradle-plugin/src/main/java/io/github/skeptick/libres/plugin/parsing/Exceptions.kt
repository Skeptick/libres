package io.github.skeptick.libres.plugin.parsing

import io.github.skeptick.libres.plugin.models.LocaleTag
import io.github.skeptick.libres.plugin.models.PluralsResource

class LocaleTagNotFoundException internal constructor(fileName: String) : Exception(
    buildString {
        appendLine("Missing locale tag in resource file name.")
        appendLine("File: $fileName")
        appendLine("Expected format: add a BCP 47 tag after the last underscore, for example 'strings_en.xml' or 'strings_zh-Hans-CN.xml'.")
    }
)

class InvalidStringResourceNameException internal constructor(resourceName: String) : Exception(
    buildString {
        appendLine("Invalid string resource name.")
        appendLine("Resource: $resourceName")
        appendLine("Allowed format: start with a Latin letter and use only Latin letters, digits, and underscores.")
    }
)

class StringResourceNameClashException internal constructor(
    localeTag: LocaleTag,
    resourceName: String
) : Exception(
    buildString {
        appendLine("Duplicate string resource name in the same locale.")
        appendLine("Locale: ${localeTag.value}")
        appendLine("Resource: $resourceName")
    }
)

class BaseStringResourcesNotFoundException internal constructor(
    resourceName: String,
    baseLocaleTag: LocaleTag,
    existLocaleTags: List<LocaleTag>
) : Exception(
    buildString {
        appendLine("Base locale '${baseLocaleTag.value}' is missing for string resource.")
        appendLine("Resource: $resourceName")
        appendLine("Available locales: ${existLocaleTags.map { it.value }}")
    }
)

class StringResourceInvalidFormatException internal constructor(
    localeTag: LocaleTag,
    resourceName: String
) : Exception(
    buildString {
        appendLine($$"Java format specifiers are not supported in string resources. Use Libres template parameters such as ${template}.")
        appendLine("Locale: ${localeTag.value}")
        appendLine("Resource: $resourceName")
    }
)

class InvalidTemplateParameterNameException internal constructor(
    localeTag: LocaleTag,
    resourceName: String,
    invalidParameters: Set<String>
) : Exception(
    buildString {
        appendLine("String resource contains invalid template parameter names.")
        appendLine("Locale: ${localeTag.value}")
        appendLine("Resource: $resourceName")
        appendLine("Invalid parameters: $invalidParameters")
        appendLine("Allowed format: start with a Latin letter and use only Latin letters, digits, and underscores.")
    }
)

class DifferentStringResourceTypesException internal constructor(
    resourceName: String
) : Exception(
    buildString {
        appendLine("String resource has inconsistent XML item types across locales.")
        appendLine("Resource: $resourceName")
        appendLine("Expected type: use either <string>, <plurals> or <string-array> consistently.")
    }
)

class PluralStringWithoutQuantityException internal constructor(
    localeTag: LocaleTag,
    resourceName: String
) : Exception(
    buildString {
        appendLine("Plural string resource has no quantity items.")
        appendLine("Locale: ${localeTag.value}")
        appendLine("Resource: $resourceName")
    }
)

class InvalidPluralQuantityException internal constructor(
    localeTag: LocaleTag,
    resourceName: String
) : Exception(
    buildString {
        appendLine("Plural string resource contains an unsupported quantity.")
        appendLine("Locale: ${localeTag.value}")
        appendLine("Resource: $resourceName")
        appendLine("Available quantities: ${PluralsResource.Quantity.entries}")
    }
)

class InvalidParametersException internal constructor(
    localeTag: LocaleTag,
    resourceName: String,
    expectedParameters: Set<String>,
    actualParameters: Set<String>
) : Exception(
    buildString {
        appendLine("Localized string parameters do not match the base locale parameters.")
        appendLine("Locale: ${localeTag.value}")
        appendLine("Resource: $resourceName")
        appendLine("Expected parameters: $expectedParameters")
        appendLine("Actual parameters: $actualParameters")
    }
)
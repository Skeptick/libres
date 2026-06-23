@file:Suppress("FunctionName")

package io.github.skeptick.libres.plugin.declarations

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.joinToCode
import io.github.skeptick.libres.plugin.models.LocaleTag
import io.github.skeptick.libres.plugin.models.PluralsResource
import io.github.skeptick.libres.plugin.models.ResourcesSettings
import io.github.skeptick.libres.plugin.models.StringResource
import io.github.skeptick.libres.plugin.models.TextResource
import io.github.skeptick.libres.plugin.models.className
import io.github.skeptick.libres.strings.PluralForms

/**
 * ```
 * public object StringsEn : Strings {
 *
 *   public val simple_string: String = "Hello!"
 *
 *   public val format_string: LibresFormatFormatString = LibresFormatFormatString("Hello, %1\$s!")
 *
 *   public val plural_string: LibresFormatPluralString = LibresFormatPluralString(
 *     PluralForms(one = "%1\$s resource", other = "%1\$s resources"), "en"
 *   )
 *
 * }
 * ```
 */
internal fun StringsLocalizedObject(
    settings: ResourcesSettings,
    localeTag: LocaleTag,
    resources: List<TextResource>
): FileSpec {
    val className = "Strings${localeTag.objectNameSuffix}"
    return FileSpec.builder(settings.stringsPackageName, className)
        .addType(
            TypeSpec.objectBuilder(className)
                .addSuperinterface(ClassName(settings.stringsPackageName, "Strings"))
                .addProperties(resources.map { resource ->
                    val className = resource.className(settings)
                    val nullable = !resource.existForLocale(localeTag)
                    PropertySpec.builder(resource.name, className.copy(nullable = nullable))
                        .addModifiers(KModifier.OVERRIDE)
                        .resourceInitializer(resource, className, localeTag)
                        .build()
                })
                .build()
        )
        .build()
}

private fun TextResource.existForLocale(localeTag: LocaleTag): Boolean {
    return when (this) {
        is StringResource -> localizedValues.containsKey(localeTag)
        is PluralsResource -> localizedItems.containsKey(localeTag)
    }
}

private fun PropertySpec.Builder.resourceInitializer(
    resource: TextResource,
    className: ClassName,
    localeTag: LocaleTag
): PropertySpec.Builder {
    return when (resource) {
        is StringResource -> when (val value = resource.localizedValues[localeTag]) {
            null -> initializer("null")
            else if className == STRING -> initializer("%L", value.unescapeXmlStringValue().toKotlinStringLiteral())
            else -> initializer("%T(%L)", className, value.unescapeXmlStringValue().toKotlinStringLiteral())
        }
        is PluralsResource -> when (val items = resource.localizedItems[localeTag]) {
            null -> initializer("null")
            else -> initializer("%T(%L, %S)", className, items.toPluralFormsCodeBlock(), localeTag.language)
        }
    }
}

/**
 * ```
 * PluralForms(one = "%1\$s resource", other = "%1\$s resources")
 * ```
 */
private fun List<PluralsResource.Item>.toPluralFormsCodeBlock(): CodeBlock {
    return buildCodeBlock {
        add("%T", PluralForms::class.asClassName())
        add(joinToCode(separator = ", ", prefix = "(", suffix = ")") { item ->
            buildCodeBlock {
                add("%L = %L", item.quantity.serialName, item.value.unescapeXmlStringValue().toKotlinStringLiteral())
            }
        })
    }
}

private fun String.unescapeXmlStringValue(): String =
    replace("\\n", "\n")
        .replace("\\r", "\r")
        .replace("\\t", "\t")
        .replace("\\\"", "\"")
        .replace("\\'", "'")
        .replace("\\\\", "\\")

private fun String.toKotlinStringLiteral(): String =
    buildString(length + 2) {
        append('"')
        for (ch in this@toKotlinStringLiteral) {
            when (ch) {
                '\\' -> append("\\\\")
                '"' -> append("\\\"")
                '$' -> append("\\$")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                '\b' -> append("\\b")
                '\u00b7', '\u2662', '\u2028', '\u2029' -> appendUnicodeEscape(ch) // KotlinPoet control chars
                in '\u0000'..'\u001F', in '\u007F'..'\u009F' -> appendUnicodeEscape(ch) // invisible control chars
                else -> append(ch)
            }
        }
        append('"')
    }

private fun StringBuilder.appendUnicodeEscape(ch: Char) =
    append("\\u").append(ch.code.toString(16).padStart(4, '0'))
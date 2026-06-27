@file:Suppress("FunctionName")

package io.github.skeptick.libres.plugin.declarations

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.joinToCode
import io.github.skeptick.libres.LibresLocale
import io.github.skeptick.libres.LibresLocalizations
import io.github.skeptick.libres.plugin.models.LocaleTag
import io.github.skeptick.libres.plugin.models.ResourcesSettings
import io.github.skeptick.libres.plugin.models.TextResource
import io.github.skeptick.libres.plugin.models.className
import io.github.skeptick.libres.plugin.common.snakeCaseToCamelCase

private val firstNotNullOfOrNull = MemberName("kotlin.collections", "firstNotNullOfOrNull")

/**
 * ```
 * @file:OptIn(InternalLibresApi::class)
 *
 * public object MainResStrings {
 *
 *   private val baseLocale: StringsEn = StringsEn
 *
 *   private val localizations: LibresLocalizations<Strings> = LibresLocalizations(
 *     LibresLocale(language = "en", script = null, region = null) to StringsEn,
 *     LibresLocale(language = "ru", script = null, region = null) to StringsRu
 *   )
 *
 *   public val simple_string: String
 *     get() = localizations.current?.firstNotNullOfOrNull { it.simple_string } ?: baseLocale.simple_string
 *
 *   public val format_string: LibresFormatFormatString
 *     get() = localizations.current?.firstNotNullOfOrNull { it.format_string } ?: baseLocale.format_string
 *
 *   public val plural_string: LibresFormatPluralString
 *     get() = localizations.current?.firstNotNullOfOrNull { it.plural_string } ?: baseLocale.plural_string
 *
 * }
 * ```
 */
internal fun StringsCommonObject(
    settings: ResourcesSettings,
    localeTags: Set<LocaleTag>,
    resources: List<TextResource>
): FileSpec {
    val className = "${settings.resourcesName}Strings"
    val baseLocaleClassName = "Strings${settings.baseLocaleTag.objectNameSuffix}"
    val stringsInterface = ClassName(settings.stringsPackageName, "Strings")
    val localizationsClass = LibresLocalizations::class.asClassName().parameterizedBy(stringsInterface)

    return FileSpec.builder(settings.stringsPackageName, className)
        .addAnnotation(InternalLibresApiAnnotation)
        .addType(
            TypeSpec.objectBuilder(className)
                .addModifiers(if (settings.generateInternalClasses) KModifier.INTERNAL else KModifier.PUBLIC)
                .applyIf(settings.camelCaseForApple) { addAnnotation(ExperimentalObjCNameAnnotation) }
                .addProperty(
                    PropertySpec.builder("baseLocale", ClassName(settings.stringsPackageName, baseLocaleClassName))
                        .addModifiers(KModifier.PRIVATE)
                        .initializer(baseLocaleClassName)
                        .build()
                )
                .addProperty(
                    PropertySpec.builder("localizations", localizationsClass)
                        .addModifiers(KModifier.PRIVATE)
                        .initializer(localeTags.toLocalizationsCodeBlock())
                        .build()
                )
                .addProperties(resources.map { resource ->
                    PropertySpec.builder(resource.name, resource.className(settings))
                        .applyIf(settings.camelCaseForApple) {
                            val customName = resource.name.snakeCaseToCamelCase(startWithLower = true)
                            addAnnotation(ObjCNameAnnotation(customName))
                        }
                        .getter(
                            FunSpec.getterBuilder()
                                .addCode(resource.toGetterCodeBlock())
                                .build()
                        )
                        .build()
                })
                .build()
        )
        .build()
}

/**
 * ```
 * LibresLocalizations(
 *   LibresLocale(language = "en", script = null, region = null) to StringsEn,
 *   LibresLocale(language = "ru", script = null, region = null) to StringsRu
 * )
 * ```
 */
private fun Set<LocaleTag>.toLocalizationsCodeBlock(): CodeBlock {
    return buildCodeBlock {
        add(
            "%T(%L)", LibresLocalizations::class.asClassName(), joinToCode(
                separator = ",\n", prefix = "\n", suffix = "\n"
            ) { localeTag ->
                buildCodeBlock {
                    val (language, script, region) = localeTag
                    add("%T", LibresLocale::class.asClassName())
                    add("(language = %S, script = %S, region = %S)", language, script, region)
                    add(" to Strings${localeTag.objectNameSuffix}")
                }
            })
    }
}

/**
 * ```
 * return localizations.current?.firstNotNullOfOrNull { it.simple_string } ?: baseLocale.simple_string
 * ```
 */
private fun TextResource.toGetterCodeBlock(): CodeBlock {
    return buildCodeBlock {
        add("return localizations.current?.%M { it.%L }", firstNotNullOfOrNull, name)
        add(" ?: baseLocale.%L", name)
    }
}

private inline fun <T, R : T> R.applyIf(`if`: Boolean, body: R.() -> T): T = if (`if`) body() else this
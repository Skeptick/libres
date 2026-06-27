@file:Suppress("FunctionName")

package io.github.skeptick.libres.plugin.declarations

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import io.github.skeptick.libres.plugin.models.PluralsResource
import io.github.skeptick.libres.plugin.models.ResourcesSettings
import io.github.skeptick.libres.plugin.models.StringResource
import io.github.skeptick.libres.plugin.models.TextResource
import io.github.skeptick.libres.plugin.models.className
import io.github.skeptick.libres.plugin.common.snakeCaseToCamelCase
import io.github.skeptick.libres.strings.PluralForms
import io.github.skeptick.libres.strings.VoidFormattedString
import io.github.skeptick.libres.strings.formatString
import io.github.skeptick.libres.strings.getPluralizedString

private val formatString = MemberName(VoidFormattedString::class.asClassName().packageName, ::formatString.name)

private val getPluralizedString = MemberName(PluralForms::class.asClassName().packageName, ::getPluralizedString.name)

internal fun StringsFormatClasses(
    settings: ResourcesSettings,
    resources: List<TextResource>
): FileSpec {
    return FileSpec.builder(settings.stringsPackageName, "FormatClasses")
        .addTypes(resources.mapNotNull { resource ->
            when {
                resource.parameters.isEmpty() -> null
                else -> when (resource) {
                    is StringResource -> CustomFormatStringClass(settings, resource)
                    is PluralsResource -> CustomFormatPluralStringClass(settings, resource)
                }
            }
        })
        .build()
}

/**
 * ```
 * public class LibresFormatFormatString(
 *   private val value: String
 * ) {
 *   public fun format(name: String): String = formatString(value, arrayOf(name))
 * }
 * ```
 */
private fun CustomFormatStringClass(
    settings: ResourcesSettings,
    resource: StringResource
): TypeSpec {
    val parameters = resource.parameters.map { it.snakeCaseToCamelCase(startWithLower = true) }
    return TypeSpec.classBuilder(resource.className(settings))
        .addModifiers(if (settings.generateInternalClasses) KModifier.INTERNAL else KModifier.PUBLIC)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("value", STRING)
                .build()
        ).addProperty(
            PropertySpec.builder("value", STRING)
                .initializer("value")
                .addModifiers(KModifier.PRIVATE)
                .build()
        ).addFunction(
            FunSpec.builder("format")
                .addParameters(parameters.map { ParameterSpec.builder(it, STRING).build() })
                .returns(STRING)
                .addStatement("return %M(value, arrayOf(%L))", formatString, parameters.joinToString(", "))
                .build()
        ).build()
}

/**
 * ```
 * public class LibresFormatPluralString(
 *   private val forms: PluralForms,
 *   private val languageCode: String
 * ) {
 *   fun format(number: Int, count: String) = formatString(getPluralizedString(forms, languageCode, number), arrayOf(count))
 * }
 * ```
 */
private fun CustomFormatPluralStringClass(
    settings: ResourcesSettings,
    resource: PluralsResource
): TypeSpec {
    val parameters = resource.parameters.map { it.snakeCaseToCamelCase(startWithLower = true) }
    return TypeSpec.classBuilder(resource.className(settings))
        .addModifiers(if (settings.generateInternalClasses) KModifier.INTERNAL else KModifier.PUBLIC)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("forms", PluralForms::class.asClassName())
                .addParameter("languageCode", STRING)
                .build()
        ).addProperty(
            PropertySpec.builder("forms", PluralForms::class.asClassName())
                .initializer("forms")
                .addModifiers(KModifier.PRIVATE)
                .build()
        ).addProperty(
            PropertySpec.builder("languageCode", STRING)
                .initializer("languageCode")
                .addModifiers(KModifier.PRIVATE)
                .build()
        ).addFunction(
            FunSpec.builder("format")
                .addParameter("number", INT)
                .addParameters(parameters.map { ParameterSpec.builder(it, STRING).build() })
                .returns(STRING)
                .addStatement(
                    "return %M(%M(forms, languageCode, number), arrayOf(%L))",
                    formatString,
                    getPluralizedString,
                    parameters.joinToString(", ")
                )
                .build()
        ).build()
}
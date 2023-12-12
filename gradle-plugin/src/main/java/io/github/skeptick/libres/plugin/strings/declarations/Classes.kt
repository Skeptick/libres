@file:Suppress("FunctionName")

package io.github.skeptick.libres.plugin.strings.declarations

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import io.github.skeptick.libres.plugin.common.declarations.addExperimentalObjCNameAnnotation
import io.github.skeptick.libres.plugin.strings.capitalizeUS
import io.github.skeptick.libres.plugin.strings.models.LanguageCode
import io.github.skeptick.libres.plugin.strings.models.PluralsResource
import io.github.skeptick.libres.plugin.strings.models.StringResource
import io.github.skeptick.libres.plugin.strings.models.TextResource
import io.github.skeptick.libres.plugin.strings.snakeCaseToCamelCase
import io.github.skeptick.libres.strings.PluralForms
import io.github.skeptick.libres.strings.formatString
import io.github.skeptick.libres.strings.getPluralizedString

/*
 * interface Strings
 */
internal fun StringsInterface(): TypeSpec.Builder {
    return TypeSpec.interfaceBuilder("Strings")
}

/*
 * object StringsRu : Strings
 */
internal fun LocalizedStringsObject(
    packageName: String,
    languageCode: LanguageCode
): TypeSpec.Builder {
    return TypeSpec.objectBuilder("Strings${languageCode.capitalizeUS()}")
        .addSuperinterface(ClassName(packageName, "Strings"))
}

/*
 * object MainResStrings {
 *     private val baseLocale: StringsRu = StringsRu
 *     private val locales: Map<String, Strings> = mapOf("ru" to StringsRu, "en" to StringsEn)
 * }
 */
internal fun StringObject(
    packageName: String,
    name: String,
    baseLanguageCode: LanguageCode,
    languageCodes: Set<LanguageCode>,
    camelCaseForApple: Boolean
): TypeSpec.Builder {
    return TypeSpec.objectBuilder("${name}Strings")
        .addExperimentalObjCNameAnnotation(camelCaseForApple)
        .addProperty(
            PropertySpec.builder("baseLocale", ClassName(packageName, "Strings${baseLanguageCode.capitalizeUS()}"))
                .addModifiers(KModifier.PRIVATE)
                .initializer("Strings${baseLanguageCode.capitalizeUS()}")
                .build()
        ).addProperty(
            PropertySpec.builder("locales", LocalesMapTypeName(packageName))
                .initializer("mapOf(%L)", languageCodes.joinToString(", ") { "\"$it\" to Strings${it.capitalizeUS()}" })
                .addModifiers(KModifier.PRIVATE)
                .build()
        )
}

/*
 * object MainResStrings
 */
internal fun EmptyStringObject(name: String): TypeSpec.Builder {
    return TypeSpec.objectBuilder("${name}Strings")
}

internal fun CustomFormattedTextResourceClass(type: ClassName, resource: TextResource): TypeSpec.Builder {
    return when (resource) {
        is StringResource -> CustomFormattedStringClass(type, resource)
        is PluralsResource -> CustomFormattedPluralStringClass(type, resource)
    }
}

/*
 * class VoidCustomFormattedString(private val value: String) {
 *     fun format(arg1: String, arg2: String): String {
 *         return formatString(value, arrayOf(arg1, arg2))
 *     }
 * }
 */
private fun CustomFormattedStringClass(type: ClassName, resource: StringResource): TypeSpec.Builder {
    val parameters = resource.parameters.map { it.snakeCaseToCamelCase(startWithLower = true) }.toSet()
    return TypeSpec.classBuilder(type.simpleName)
        .primaryConstructor(
            FunSpec.constructorBuilder().addParameter("value", String::class).build()
        ).addProperty(
            PropertySpec.builder("value", String::class).initializer("value").addModifiers(KModifier.PRIVATE).build()
        ).addFunction(
            FunSpec.builder("format")
                .addParameters(parameters.map { ParameterSpec.builder(it, String::class).build() })
                .returns(String::class)
                .addStatement("return %L(value, arrayOf(%L))", ::formatString.name, parameters.joinToString(","))
                .build()
        )
}

/*
 * class VoidCustomFormattedPluralString(private val forms: PluralForms, private val languageCode: String) {
 *     fun format(number: Int, arg1: String, arg2: String) {
 *         return formatString(getPluralizedString(forms, languageCode, number), arrayOf(arg1, arg2))
 *     }
 * }
 */
private fun CustomFormattedPluralStringClass(type: ClassName, resource: PluralsResource): TypeSpec.Builder {
    val parameters = resource.parameters.map { it.snakeCaseToCamelCase(startWithLower = true) }.toSet()
    return TypeSpec.classBuilder(type.simpleName)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("forms", PluralForms::class.asClassName())
                .addParameter("languageCode", String::class)
                .build()
        ).addProperty(
            PropertySpec.builder("forms", PluralForms::class.asClassName()).initializer("forms").addModifiers(KModifier.PRIVATE).build()
        ).addProperty(
            PropertySpec.builder("languageCode", String::class).initializer("languageCode").addModifiers(KModifier.PRIVATE).build()
        ).addFunction(
            FunSpec.builder("format")
                .addParameter("number", Int::class)
                .addParameters(parameters.map { ParameterSpec.builder(it, String::class).build() })
                .returns(String::class)
                .addStatement("return %L(%L(forms, languageCode, number), arrayOf(%L))", ::formatString.name, ::getPluralizedString.name, parameters.joinToString(","))
                .build()
        )
}

private fun LocalesMapTypeName(packageName: String): TypeName {
    return Map::class.asClassName().parameterizedBy(String::class.asClassName(), ClassName(packageName, "Strings"))
}
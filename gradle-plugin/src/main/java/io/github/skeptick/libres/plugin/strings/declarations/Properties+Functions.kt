package io.github.skeptick.libres.plugin.strings.declarations

import com.squareup.kotlinpoet.*
import io.github.skeptick.libres.plugin.common.declarations.addObjCNameAnnotation
import io.github.skeptick.libres.plugin.strings.models.LanguageCode
import io.github.skeptick.libres.plugin.strings.models.PluralsResource
import io.github.skeptick.libres.plugin.strings.models.StringResource
import io.github.skeptick.libres.plugin.strings.models.TextResource
import io.github.skeptick.libres.plugin.strings.snakeCaseToCamelCase
import io.github.skeptick.libres.plugin.strings.unescapeXml
import io.github.skeptick.libres.strings.PluralForms
import io.github.skeptick.libres.strings.getCurrentLanguageCode

/*
 * val string_name: ClassName?
 */
internal fun TypeSpec.Builder.addTextResourceToInterface(
    name: String,
    type: ClassName
): TypeSpec.Builder {
    return addProperty(
        PropertySpec.builder(name, type.copy(nullable = true)).build()
    )
}

/*
 * for string: override val string_name: ClassName = ClassName(value)
 * for plural: override val plural_name: ClassName = ClassName(PluralForms("one", "two"), "ru")
 */
internal fun TypeSpec.Builder.addTextResourceToLocalizedObject(
    name: String,
    resource: TextResource?,
    type: ClassName,
    languageCode: LanguageCode
): TypeSpec.Builder {
    return addProperty(
        PropertySpec.builder(name, type.copy(nullable = resource == null))
            .addModifiers(KModifier.OVERRIDE)
            .apply {
                when (resource) {
                    is PluralsResource -> initializer("%L(%L, %S)", type.simpleName, resource.items.toPluralFormsInitializer(), languageCode)
                    is StringResource -> when (type.simpleName) {
                        "String" -> initializer("%S", resource.value.unescapeXml())
                        else -> initializer("%L(%S)", type.simpleName, resource.value.unescapeXml())
                    }
                    else -> initializer("null")
                }
            }.build()
    )
}

/*
 * fun string_name(locale: String): ClassName {
 *     return locales[locale]?.string_name ?: baseLocale.string_name
 * }
 */
internal fun TypeSpec.Builder.addTextResourceFunctionToStringsObject(
    name: String,
    type: ClassName
): TypeSpec.Builder {
    return addFunction(
        FunSpec.builder(name).apply {
            addParameter("locale", String::class)
            addCode("return locales[locale]?.$name ?: baseLocale.$name")
            returns(type)
        }.build()
    )
}

/*
 * val string_name: ClassName
 *    get() = locales[getCurrentLanguageCode()]?.string_name ?: baseLocale.string_name
 */
internal fun TypeSpec.Builder.addTextResourceToStringsObject(
    name: String,
    type: ClassName,
    camelCaseForApple: Boolean
): TypeSpec.Builder {
    return addProperty(
        PropertySpec.builder(name, type)
            .getter(
                FunSpec.getterBuilder()
                    .addStatement("return $name(${::getCurrentLanguageCode.name}())")
                    .build()
            ).addObjCNameAnnotation(camelCaseForApple) {
                name.snakeCaseToCamelCase(startWithLower = true)
            }.build()
    )
}

private fun List<PluralsResource.Item>.toPluralFormsInitializer(): CodeBlock {
    val builder = CodeBlock.builder()
    builder.add(PluralForms::class.simpleName!!)
    builder.add("(")
    for (item in this) builder.add("%L = %S, ", item.quantity.serialName, item.value.unescapeXml())
    builder.add(")")
    return builder.build()
}
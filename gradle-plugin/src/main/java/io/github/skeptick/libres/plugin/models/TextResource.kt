package io.github.skeptick.libres.plugin.models

import com.squareup.kotlinpoet.ARRAY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import io.github.skeptick.libres.plugin.parsing.InvalidParametersException
import io.github.skeptick.libres.plugin.common.extractInterpolationParameters
import io.github.skeptick.libres.plugin.common.replaceTemplateParameters
import io.github.skeptick.libres.plugin.common.snakeCaseToCamelCase
import io.github.skeptick.libres.strings.VoidFormattedPluralString
import io.github.skeptick.libres.strings.VoidFormattedString
import io.github.skeptick.libres.strings.VoidPluralString

internal sealed interface TextResource {
    val name: String
    val parameters: Set<String>
}

internal fun TextResource.replaceParametersToJavaSpecifiers(value: String, locale: LocaleTag): String {
    val actualParameters = value.extractInterpolationParameters().toSet()
    if (!parameters.containsAll(actualParameters)) {
        throw InvalidParametersException(
            localeTag = locale,
            resourceName = name,
            expectedParameters = parameters,
            actualParameters = actualParameters
        )
    } else {
        return value.replaceTemplateParameters { _, argument ->
            $$"%$${parameters.indexOf(argument) + 1}$s"
        }
    }
}

internal fun TextResource.className(settings: ResourcesSettings): ClassName {
    return when (this) {
        is StringResource, is ArrayResource -> when {
            parameters.isEmpty() -> STRING
            settings.generateNamedArguments -> ClassName(settings.stringsPackageName, "LibresFormat" + name.snakeCaseToCamelCase())
            else -> VoidFormattedString::class.asClassName()
        }
        is PluralsResource -> when {
            parameters.isEmpty() -> VoidPluralString::class.asClassName()
            settings.generateNamedArguments -> ClassName(settings.stringsPackageName, "LibresFormat" + name.snakeCaseToCamelCase())
            else -> VoidFormattedPluralString::class.asClassName()
        }
    }
}

internal fun TextResource.typeName(settings: ResourcesSettings): TypeName {
    return when (this) {
        is StringResource -> className(settings)
        is PluralsResource -> className(settings)
        is ArrayResource -> ARRAY.parameterizedBy(className(settings))
    }
}
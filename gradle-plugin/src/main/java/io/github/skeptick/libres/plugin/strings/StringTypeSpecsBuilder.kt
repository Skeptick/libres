@file:Suppress("CanBeParameter")

package io.github.skeptick.libres.plugin.strings

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import io.github.skeptick.libres.plugin.common.declarations.saveTo
import io.github.skeptick.libres.plugin.strings.declarations.*
import io.github.skeptick.libres.plugin.strings.models.*
import io.github.skeptick.libres.strings.*
import org.gradle.api.file.Directory

internal class StringTypeSpecsBuilder(
    private val outputPackageName: String,
    private val outputClassName: String,
    private val languageCodes: Set<LanguageCode>,
    private val baseLanguageCode: String,
    private val generateNamedArguments: Boolean,
    private val camelCaseForApple: Boolean,
) {

    private val stringsInterface = StringsInterface()
    private val localizedObjects = languageCodes.associateWith { LocalizedStringsObject(outputPackageName, it) }
    private val baseLocalizedObject = localizedObjects.getValue(baseLanguageCode)
    private val stringObject = StringObject(outputPackageName, outputClassName, baseLanguageCode, languageCodes, camelCaseForApple)
    private val customClasses = mutableListOf<TypeSpec.Builder>()

    fun appendResource(baseResource: TextResource, localizedResources: Map<LanguageCode, TextResource?>) {
        val resourceName = baseResource.name
        val baseParameters = baseResource.parameters
        val formattedResource = baseResource.replaceParameters(baseParameters, baseLanguageCode)
        val type = baseResource.fetchClass(outputPackageName, baseParameters.isNotEmpty(), generateNamedArguments)

        baseResource.appendLocalizedResources(localizedResources, type)
        stringsInterface.addTextResourceToInterface(resourceName, type)
        baseLocalizedObject.addTextResourceToLocalizedObject(resourceName, formattedResource, type, baseLanguageCode)
        stringObject.addTextResourceToStringsObject(resourceName, type, camelCaseForApple)
        if (generateNamedArguments && baseParameters.isNotEmpty()) customClasses += CustomFormattedTextResourceClass(type, baseResource)
    }

    fun save(outputDirectory: Directory) {
        StringsInterfaceFile(outputPackageName, stringsInterface).saveTo(outputDirectory)
        StringsObjectFile(outputPackageName, stringObject).saveTo(outputDirectory)
        localizedObjects.forEach { StringsObjectFile(outputPackageName, it.value).saveTo(outputDirectory) }
        if (customClasses.isNotEmpty()) FormattedClassesFile(outputPackageName, customClasses).saveTo(outputDirectory)
    }

    private fun TextResource.appendLocalizedResources(localizedResources: Map<LanguageCode, TextResource?>, type: ClassName) {
        for (languageCode in languageCodes) {
            if (languageCode == baseLanguageCode) continue
            val localizedObject = localizedObjects.getValue(languageCode)
            when (val localizedResource = localizedResources[languageCode]) {
                null -> localizedObject.addTextResourceToLocalizedObject(name, null, type, languageCode)
                else -> localizedResource.replaceParameters(parameters, languageCode).also {
                    localizedObject.addTextResourceToLocalizedObject(name, it, type, languageCode)
                }
            }
        }
    }

}

private fun TextResource.fetchClass(packageName: String, hasParameters: Boolean, generateNamedArguments: Boolean) =
    when (this) {
        is StringResource -> when {
            !hasParameters -> String::class.asTypeName()
            generateNamedArguments -> ClassName(packageName, "LibresFormat" + name.snakeCaseToCamelCase())
            else -> VoidFormattedString::class.asTypeName()
        }
        is PluralsResource -> when {
            !hasParameters -> VoidPluralString::class.asTypeName()
            generateNamedArguments -> ClassName(packageName, "LibresFormat" + name.snakeCaseToCamelCase())
            else -> VoidFormattedPluralString::class.asTypeName()
        }
    }

private fun TextResource.replaceParameters(parameters: Set<String>, languageCode: LanguageCode) =
    when (this) {
        is StringResource -> copy(
            value = value.replaceNamedArguments { _, argument ->
                val actualIndex = parameters.indexOf(argument)
                if (actualIndex != -1) $$"%$${actualIndex + 1}$s"
                else throw ParameterNotFoundException(languageCode, name, argument)
            }
        )
        is PluralsResource -> copy(
            items = items.map { item ->
                item.copy(
                    value = item.value.replaceNamedArguments { _, argument ->
                        val actualIndex = parameters.indexOf(argument)
                        if (actualIndex != -1) "%${actualIndex + 1}\$s"
                        else throw ParameterNotFoundException(languageCode, name, argument)
                    }
                )
            }
        )
    }
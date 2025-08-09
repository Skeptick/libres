package io.github.skeptick.libres.plugin

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider
import javax.inject.Inject

@Suppress("unused")
abstract class ResourcesPluginExtension @Inject constructor(objects: ObjectFactory) {

    internal val generatedClassNameProp = objects.property(String::class.java).convention("Res")

    internal val generateNamedArgumentsProp = objects.property(Boolean::class.java).convention(false)

    internal val baseLocaleLanguageCodeProp = objects.property(String::class.java).convention("en")

    internal val camelCaseNamesForAppleFrameworkProp = objects.property(Boolean::class.java).convention(false)

    var generatedClassName: String
        get() = generatedClassNameProp.get()
        set(value) = generatedClassNameProp.set(value)

    var generateNamedArguments: Boolean
        get() = generateNamedArgumentsProp.get()
        set(value) = generateNamedArgumentsProp.set(value)

    var baseLocaleLanguageCode: String
        get() = baseLocaleLanguageCodeProp.get()
        set(value) = baseLocaleLanguageCodeProp.set(value)

    var camelCaseNamesForAppleFramework: Boolean
        get() = camelCaseNamesForAppleFrameworkProp.get()
        set(value) = camelCaseNamesForAppleFrameworkProp.set(value)

    fun setGeneratedClassName(provider: Provider<String>) {
        generatedClassNameProp.set(provider)
    }

    fun setGenerateNamedArguments(provider: Provider<Boolean>) {
        generateNamedArgumentsProp.set(provider)
    }

    fun setBaseLocaleLanguageCode(provider: Provider<String>) {
        baseLocaleLanguageCodeProp.set(provider)
    }

    fun setCamelCaseNamesForAppleFrameworkProp(provider: Provider<Boolean>) {
        camelCaseNamesForAppleFrameworkProp.set(provider)
    }

    internal fun finalizeValuesOnRead() {
        generatedClassNameProp.finalizeValueOnRead()
        generateNamedArgumentsProp.finalizeValueOnRead()
        baseLocaleLanguageCodeProp.finalizeValueOnRead()
        camelCaseNamesForAppleFrameworkProp.finalizeValueOnRead()
    }

}
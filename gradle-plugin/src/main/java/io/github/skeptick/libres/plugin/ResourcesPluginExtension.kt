package io.github.skeptick.libres.plugin

import io.github.skeptick.libres.plugin.models.LocaleTag
import io.github.skeptick.libres.plugin.models.ResourcesSettings
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import javax.inject.Inject

@Suppress("unused")
abstract class ResourcesPluginExtension @Inject constructor(objects: ObjectFactory) {

    internal val generatedClassNameProp = objects.property(String::class.java).convention("Res")

    internal val generateNamedArgumentsProp = objects.property(Boolean::class.java).convention(false)

    internal val baseLocaleTagProp = objects.property(String::class.java).convention("en")

    internal val camelCaseNamesForAppleFrameworkProp = objects.property(Boolean::class.java).convention(false)

    internal val generatedClassPackageNameProp = objects.property(String::class.java)

    var generatedClassName: String
        get() = generatedClassNameProp.get()
        set(value) = generatedClassNameProp.set(value)

    var generatedClassPackageName: String?
        get() = generatedClassPackageNameProp.orNull
        set(value) = generatedClassPackageNameProp.set(value)

    var generateNamedArguments: Boolean
        get() = generateNamedArgumentsProp.get()
        set(value) = generateNamedArgumentsProp.set(value)

    var baseLocaleTag: String
        get() = baseLocaleTagProp.get()
        set(value) = baseLocaleTagProp.set(value)

    var camelCaseNamesForAppleFramework: Boolean
        get() = camelCaseNamesForAppleFrameworkProp.get()
        set(value) = camelCaseNamesForAppleFrameworkProp.set(value)

    fun setGeneratedClassName(provider: Provider<String>) {
        generatedClassNameProp.set(provider)
    }

    fun setGeneratedClassPackageName(provider: Provider<String>) {
        generatedClassPackageNameProp.set(provider)
    }

    fun setGenerateNamedArguments(provider: Provider<Boolean>) {
        generateNamedArgumentsProp.set(provider)
    }

    fun setBaseLocaleTag(provider: Provider<String>) {
        baseLocaleTagProp.set(provider)
    }

    fun setCamelCaseNamesForAppleFrameworkProp(provider: Provider<Boolean>) {
        camelCaseNamesForAppleFrameworkProp.set(provider)
    }

    internal fun finalizeValuesOnRead() {
        generatedClassNameProp.finalizeValueOnRead()
        generatedClassPackageNameProp.finalizeValueOnRead()
        generateNamedArgumentsProp.finalizeValueOnRead()
        baseLocaleTagProp.finalizeValueOnRead()
        camelCaseNamesForAppleFrameworkProp.finalizeValueOnRead()
    }

}

internal open class ResourcesSettingsInput @Inject constructor(objects: ObjectFactory) {

    @get:Input
    internal val resourcesName: Property<String> = objects.property(String::class.java)

    @get:Input
    internal val packageName: Property<String> = objects.property(String::class.java)

    @get:Input
    internal val generateNamedArguments: Property<Boolean> = objects.property(Boolean::class.java)

    @get:Input
    internal val camelCaseForApple: Property<Boolean> = objects.property(Boolean::class.java)

    @get:Input
    internal val baseLocaleTag: Property<String> = objects.property(String::class.java)

    internal fun toSettings(): ResourcesSettings {
        return ResourcesSettings(
            resourcesName = resourcesName.get(),
            packageName = packageName.get(),
            generateNamedArguments = generateNamedArguments.get(),
            camelCaseForApple = camelCaseForApple.get(),
            baseLocaleTag = LocaleTag(baseLocaleTag.get())
        )
    }

}
package io.github.skeptick.libres.plugin

import io.github.skeptick.libres.plugin.strings.models.LanguageCode
import java.io.Serializable

open class ResourcesPluginExtension {
    var generatedClassName: String = "Res"
    var generateNamedArguments: Boolean = false
    var baseLocaleLanguageCode: String = "en"
    var camelCaseNamesForAppleFramework: Boolean = false
}

internal data class ResourcesSettings(
    val outputPackageName: String,
    val outputClassName: String,
    val stringsPackageName: String,
    val imagesPackageName: String
) : Serializable

internal data class StringsSettings(
    val outputPackageName: String,
    val outputClassName: String,
    val generateNamedArguments: Boolean,
    val baseLocaleLanguageCode: LanguageCode,
    val camelCaseForApple: Boolean
) : Serializable

internal data class ImagesSettings(
    val outputPackageName: String,
    val outputClassName: String,
    val camelCaseForApple: Boolean,
    val appleBundleName: String
) : Serializable
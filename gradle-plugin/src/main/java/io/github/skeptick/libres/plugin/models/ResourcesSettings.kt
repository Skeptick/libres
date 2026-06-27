package io.github.skeptick.libres.plugin.models

internal data class ResourcesSettings(
    val resourcesName: String,
    val packageName: String,
    val generateNamedArguments: Boolean,
    val generateInternalClasses: Boolean,
    val camelCaseForApple: Boolean,
    val baseLocaleTag: LocaleTag
) {

    val stringsPackageName: String
        get() = "$packageName.strings"

}
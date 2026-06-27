@file:Suppress("FunctionName")

package io.github.skeptick.libres.plugin.declarations

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import io.github.skeptick.libres.plugin.models.ResourcesSettings

/**
 * ```
 * object MainResStrings
 * ```
 */
internal fun StringsEmptyObject(
    settings: ResourcesSettings
): FileSpec {
    val className = "${settings.resourcesName}Strings"
    return FileSpec.builder(settings.stringsPackageName, className)
        .addType(
            TypeSpec.objectBuilder(className)
                .addModifiers(if (settings.generateInternalClasses) KModifier.INTERNAL else KModifier.PUBLIC)
                .build()
        )
        .build()
}
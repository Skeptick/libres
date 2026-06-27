@file:Suppress("FunctionName")

package io.github.skeptick.libres.plugin.declarations

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import io.github.skeptick.libres.plugin.models.ResourcesSettings

/**
 * ```
 * object MainRes {
 *   val string: MainResStrings = MainResStrings
 * }
 * ```
 */
internal fun ResourcesObject(
    settings: ResourcesSettings
): FileSpec {
    return FileSpec.builder(settings.packageName, settings.resourcesName)
        .addType(
            TypeSpec.objectBuilder(settings.resourcesName)
                .addModifiers(if (settings.generateInternalClasses) KModifier.INTERNAL else KModifier.PUBLIC)
                .addProperty(
                    PropertySpec.builder("string", ClassName(settings.stringsPackageName, "${settings.resourcesName}Strings"))
                        .initializer("${settings.resourcesName}Strings")
                        .build()
                )
                .build()
        )
        .build()
}
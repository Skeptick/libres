@file:Suppress("FunctionName")

package io.github.skeptick.libres.plugin.common.declarations

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

/*
 * object MainRes {
 *     val string: MainResStrings = MainResStrings
 * }
 */
internal fun ResourcesObject(
    name: String,
    stringsPackageName: String,
): TypeSpec.Builder {
    return TypeSpec.objectBuilder(name)
        .addProperty(
            PropertySpec.builder("string", ClassName(stringsPackageName, "${name}Strings"))
                .initializer("${name}Strings")
                .build()
        )
}
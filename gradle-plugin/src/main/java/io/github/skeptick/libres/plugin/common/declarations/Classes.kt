@file:Suppress("FunctionName")

package io.github.skeptick.libres.plugin.common.declarations

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

/*
 * object MainRes {
 *     val string: MainResStrings = MainResStrings
 *     val image: MainResImages = MainResImages
 * }
 */
internal fun ResourcesObject(
    name: String,
    stringsPackageName: String,
    imagesPackageName: String
): TypeSpec.Builder {
    return TypeSpec.objectBuilder(name)
        .addProperty(
            PropertySpec.builder("string", ClassName(stringsPackageName, "${name}Strings"))
                .initializer("${name}Strings")
                .build()
        ).addProperty(
            PropertySpec.builder("image", ClassName(imagesPackageName, "${name}Images"))
                .initializer("${name}Images")
                .build()
        )
}
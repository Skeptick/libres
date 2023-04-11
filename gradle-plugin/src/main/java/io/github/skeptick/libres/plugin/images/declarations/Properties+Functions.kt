package io.github.skeptick.libres.plugin.images.declarations

import com.squareup.kotlinpoet.*
import io.github.skeptick.libres.plugin.KotlinPlatform
import io.github.skeptick.libres.plugin.ResourcesPlugin
import io.github.skeptick.libres.plugin.common.declarations.addObjCNameAnnotation
import io.github.skeptick.libres.plugin.images.models.ImageProps
import io.github.skeptick.libres.plugin.strings.snakeCaseToCamelCase

private val Image = ClassName(ResourcesPlugin.IMAGES_PACKAGE_NAME, "Image")

internal fun TypeSpec.Builder.appendImage(
    specs: ImageProps,
    platform: KotlinPlatform,
    hasCommon: Boolean,
    camelCaseForApple: Boolean
): TypeSpec.Builder {
    return when {
        platform == KotlinPlatform.Common -> appendExpectImage(specs)
        platform == KotlinPlatform.Apple && hasCommon -> appendActualImage(specs, specs.initializerLiteral(platform), camelCaseForApple)
        platform == KotlinPlatform.Apple -> appendImage(specs, specs.initializerLiteral(platform), camelCaseForApple)
        !hasCommon -> appendImage(specs, specs.initializerLiteral(platform), camelCaseForApple = false)
        else -> appendActualImage(specs, specs.initializerLiteral(platform), camelCaseForApple = false)
    }
}

/**
 * expect val image_name: Image
 */
private fun TypeSpec.Builder.appendExpectImage(specs: ImageProps): TypeSpec.Builder {
    return addProperty(
        PropertySpec.builder(specs.name, Image)
            .addModifiers(KModifier.EXPECT)
            .build()
    )
}

/**
 * actual val image_name: Image
 *     get() = "image_name"
 */
private fun TypeSpec.Builder.appendActualImage(
    specs: ImageProps,
    initializerLiteral: String,
    camelCaseForApple: Boolean
): TypeSpec.Builder {
    return addProperty(
        PropertySpec.builder(specs.name, Image)
            .addModifiers(KModifier.ACTUAL)
            .getter(
                FunSpec.getterBuilder()
                    .addStatement("return $initializerLiteral")
                    .build()
            ).addObjCNameAnnotation(camelCaseForApple) {
                specs.name.snakeCaseToCamelCase(startWithLower = true)
            }.build()
    )
}

/**
 * val image_name: Image
 *     get() = "image_name"
 */
private fun TypeSpec.Builder.appendImage(
    specs: ImageProps,
    initializerLiteral: String,
    camelCaseForApple: Boolean
): TypeSpec.Builder {
    return addProperty(
        PropertySpec.builder(specs.name, Image)
            .getter(
                FunSpec.getterBuilder()
                    .addStatement("return $initializerLiteral")
                    .build()
            ).addObjCNameAnnotation(camelCaseForApple) {
                specs.name.snakeCaseToCamelCase(startWithLower = true)
            }.build()
    )
}

private fun ImageProps.initializerLiteral(platform: KotlinPlatform): String =
    when (platform) {
        KotlinPlatform.Android -> "R.drawable.$name"
        KotlinPlatform.Apple -> "bundle.image(\"$name\", \"$extension\")"
        KotlinPlatform.Js, KotlinPlatform.Jvm -> "\"images/$name.$extension\""
        KotlinPlatform.Common -> throw IllegalArgumentException()
    }
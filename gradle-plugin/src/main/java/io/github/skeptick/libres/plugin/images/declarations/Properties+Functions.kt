package io.github.skeptick.libres.plugin.images.declarations

import com.squareup.kotlinpoet.*
import io.github.skeptick.libres.plugin.KotlinPlatform
import io.github.skeptick.libres.plugin.ResourcesPlugin
import io.github.skeptick.libres.plugin.images.models.ImageProps

@Suppress("PrivatePropertyName")
private val Image = ClassName(ResourcesPlugin.IMAGES_PACKAGE_NAME, "Image")

internal fun TypeSpec.Builder.appendImage(
    specs: ImageProps,
    platform: KotlinPlatform,
    hasCommon: Boolean,
    camelCaseForApple: Boolean
): TypeSpec.Builder {
    return when {
        platform == KotlinPlatform.Common -> appendExpectImage(specs.name)
        !hasCommon -> appendImage(specs.name, specs.initializerLiteral(platform))
        else -> appendActualImage(specs.name, specs.initializerLiteral(platform))
    }
}

/**
 * expect val image_name: Image
 */
private fun TypeSpec.Builder.appendExpectImage(name: String): TypeSpec.Builder {
    return addProperty(
        PropertySpec.builder(name, Image)
            .addModifiers(KModifier.EXPECT)
            .build()
    )
}

/**
 * actual val image_name: Image
 *     get() = "image_name"
 */
private fun TypeSpec.Builder.appendActualImage(
    name: String,
    initializerLiteral: String,
    camelCaseForApple: Boolean
): TypeSpec.Builder {
    return addProperty(
        PropertySpec.builder(name, Image)
            .addModifiers(KModifier.ACTUAL)
            .getter(
                FunSpec.getterBuilder()
                    .addStatement("return $initializerLiteral", name)
                    .build()
            )
            .build()
    )
}

/**
 * val image_name: Image
 *     get() = "image_name"
 */
private fun TypeSpec.Builder.appendImage(
    name: String,
    initializerLiteral: String,
    camelCaseForApple: Boolean
): TypeSpec.Builder {
    return addProperty(
        PropertySpec.builder(name, Image)
            .getter(
                FunSpec.getterBuilder()
                    .addStatement("return $initializerLiteral", name)
                    .build()
            )
            .build()
    )
}

private fun ImageProps.initializerLiteral(platform: KotlinPlatform): String =
    when (platform) {
        KotlinPlatform.Android -> "R.drawable.%L"
        KotlinPlatform.Apple -> "bundle.image(%S)"
        KotlinPlatform.Js, KotlinPlatform.Jvm -> "\"images/%L.${extension}\""
        KotlinPlatform.Common -> throw IllegalArgumentException()
    }
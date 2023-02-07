@file:Suppress("FunctionName")

package io.github.skeptick.libres.plugin.images.declarations

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import io.github.skeptick.libres.plugin.KotlinPlatform

@Suppress("PrivatePropertyName")
private val NSBundle = ClassName("platform.Foundation", "NSBundle")

internal fun ImagesObject(name: String, platform: KotlinPlatform, hasCommon: Boolean, appleBundleName: String): TypeSpec.Builder {
    return when {
        platform == KotlinPlatform.Common -> ExpectImagesObject(name)
        platform == KotlinPlatform.Apple && hasCommon -> ActualImagesObjectApple(name, appleBundleName)
        platform == KotlinPlatform.Apple -> ImagesObjectApple(name, appleBundleName)
        !hasCommon -> ImagesObject(name)
        else -> ActualImagesObject(name)
    }
}

internal fun EmptyImagesObject(name: String, platform: KotlinPlatform, hasCommon: Boolean): TypeSpec.Builder {
    return when {
        platform == KotlinPlatform.Common -> ExpectImagesObject(name)
        !hasCommon -> ImagesObject(name)
        else -> ActualImagesObject(name)
    }
}

/**
 * expect object MainResImages
 */
private fun ExpectImagesObject(name: String): TypeSpec.Builder {
    return TypeSpec.objectBuilder("${name}Images")
        .addModifiers(KModifier.EXPECT)
}

/**
 * actual object MainResImages
 */
private fun ActualImagesObject(name: String): TypeSpec.Builder {
    return TypeSpec.objectBuilder("${name}Images")
        .addModifiers(KModifier.ACTUAL)
}

/**
 * actual object MainResImages {
 *     private val bundle: NSBundle = NSBundle.bundleWithName(bundleName)
 * }
 */
private fun ActualImagesObjectApple(name: String, bundleName: String): TypeSpec.Builder {
    return TypeSpec.objectBuilder("${name}Images")
        .addModifiers(KModifier.ACTUAL)
        .appendAppleBundleProperty(bundleName)
}

/**
 * object MainResImages
 */
private fun ImagesObject(name: String): TypeSpec.Builder {
    return TypeSpec.objectBuilder("${name}Images")
}

/**
 * object MainResImages {
 *     private val bundle: NSBundle = NSBundle.bundleWithName(bundleName)
 * }
 */
private fun ImagesObjectApple(name: String, bundleName: String): TypeSpec.Builder {
    return TypeSpec.objectBuilder("${name}Images")
        .appendAppleBundleProperty(bundleName)
}

private fun TypeSpec.Builder.appendAppleBundleProperty(bundleName: String): TypeSpec.Builder {
    return addProperty(
        PropertySpec.builder("bundle", NSBundle)
            .addModifiers(KModifier.PRIVATE)
            .initializer("NSBundle.bundleWithName(%S)", bundleName)
            .build()
    )
}
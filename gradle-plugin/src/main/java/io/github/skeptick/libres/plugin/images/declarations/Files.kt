@file:Suppress("FunctionName")

package io.github.skeptick.libres.plugin.images.declarations

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import io.github.skeptick.libres.plugin.KotlinPlatform
import io.github.skeptick.libres.plugin.ResourcesPlugin

internal fun ImagesObjectFile(
    packageName: String,
    imagesObjectBuilder: TypeSpec.Builder,
    platform: KotlinPlatform
): FileSpec {
    return imagesObjectBuilder.build().let { imagesObject ->
        FileSpec.builder(packageName, imagesObject.name!!)
            .addImport(ResourcesPlugin.IMAGES_PACKAGE_NAME, "Image")
            .apply {
                when (platform) {
                    KotlinPlatform.Apple -> addImport(ResourcesPlugin.IMAGES_PACKAGE_NAME, "bundleWithName", "image")
                    KotlinPlatform.Android -> addImport(packageName.substringBeforeLast('.'), "R")
                    else -> Unit
                }
            }
            .addType(imagesObject)
            .build()
    }
}
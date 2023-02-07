@file:Suppress("CanBeParameter")

package io.github.skeptick.libres.plugin.images

import io.github.skeptick.libres.plugin.KotlinPlatform
import io.github.skeptick.libres.plugin.common.declarations.saveToDirectory
import io.github.skeptick.libres.plugin.images.declarations.ImagesObject
import io.github.skeptick.libres.plugin.images.declarations.ImagesObjectFile
import io.github.skeptick.libres.plugin.images.declarations.appendImage
import io.github.skeptick.libres.plugin.images.models.ImageProps
import java.io.File

internal class ImagesTypeSpecsBuilder(
    private val outputPackageName: String,
    private val outputClassName: String,
    private val platforms: Set<KotlinPlatform>,
    private val appleBundleName: String
) {

    private var imagesObjects = platforms.associateWith {
        ImagesObject(outputClassName, it, KotlinPlatform.Common in platforms, appleBundleName)
    }

    fun appendImage(imageProps: ImageProps) {
        imagesObjects.forEach { (platform, imagesObject) ->
            imagesObject.appendImage(imageProps, platform, KotlinPlatform.Common in platforms)
        }
    }

    fun save(directories: Map<KotlinPlatform, File>) {
        imagesObjects.forEach { (platform, imagesObject) ->
            val directory = directories.getValue(platform)
            ImagesObjectFile(outputPackageName, imagesObject, platform).saveToDirectory(directory)
        }
    }

}
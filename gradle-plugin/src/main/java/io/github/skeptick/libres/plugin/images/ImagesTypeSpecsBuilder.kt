@file:Suppress("CanBeParameter")

package io.github.skeptick.libres.plugin.images

import io.github.skeptick.libres.plugin.ImagesSettings
import io.github.skeptick.libres.plugin.KotlinPlatform
import io.github.skeptick.libres.plugin.common.declarations.saveToDirectory
import io.github.skeptick.libres.plugin.images.declarations.ImagesObject
import io.github.skeptick.libres.plugin.images.declarations.ImagesObjectFile
import io.github.skeptick.libres.plugin.images.declarations.appendImage
import io.github.skeptick.libres.plugin.images.models.ImageProps
import java.io.File

internal class ImagesTypeSpecsBuilder(
    private val settings: ImagesSettings,
    private val platforms: Set<KotlinPlatform>
) {

    private val packageName = settings.outputPackageName
    private val className = settings.outputClassName
    private val camelCaseForApple = settings.camelCaseForApple
    private val appleBundleName = settings.appleBundleName
    private val hasCommon = KotlinPlatform.Common in platforms

    private var imagesObjects = platforms.associateWith {
        ImagesObject(className, it, hasCommon, appleBundleName, camelCaseForApple)
    }

    fun appendImage(imageProps: ImageProps) {
        imagesObjects.forEach { (platform, imagesObject) ->
            imagesObject.appendImage(imageProps, platform, hasCommon, camelCaseForApple)
        }
    }

    fun save(directories: Map<KotlinPlatform, File>) {
        imagesObjects.forEach { (platform, imagesObject) ->
            val directory = directories.getValue(platform)
            ImagesObjectFile(packageName, imagesObject, platform).saveToDirectory(directory)
        }
    }

}
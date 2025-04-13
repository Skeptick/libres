package io.github.skeptick.libres.plugin

import io.github.skeptick.libres.plugin.common.declarations.saveToDirectory
import io.github.skeptick.libres.plugin.common.extensions.deleteFilesInDirectory
import io.github.skeptick.libres.plugin.images.ImagesTypeSpecsBuilder
import io.github.skeptick.libres.plugin.images.declarations.EmptyImagesObject
import io.github.skeptick.libres.plugin.images.declarations.ImagesObjectFile
import io.github.skeptick.libres.plugin.images.models.ImageProps
import io.github.skeptick.libres.plugin.images.models.ImageSet
import io.github.skeptick.libres.plugin.images.processing.removeImage
import io.github.skeptick.libres.plugin.images.processing.saveImage
import io.github.skeptick.libres.plugin.images.processing.saveImageSet
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*
import org.gradle.work.ChangeType
import org.gradle.work.Incremental
import org.gradle.work.InputChanges
import java.io.File

@CacheableTask
abstract class LibresImagesGenerationTask : DefaultTask() {

    @get:Input
    internal abstract var settings: ImagesSettings

    @get:Incremental
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    internal abstract var inputDirectory: FileCollection

    @get:OutputDirectories
    internal abstract var outputSourcesDirectories: Map<KotlinPlatform, File>

    @get:OutputDirectories
    internal abstract var outputResourcesDirectories: Map<KotlinPlatform, File>

    @TaskAction
    fun apply(inputChanges: InputChanges) {
        // Update images for changed files
        inputChanges.getFileChanges(inputDirectory)
            .forEach { change ->
                val image = ImageProps(change.file)

                @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
                when (change.changeType) {
                    ChangeType.MODIFIED, ChangeType.ADDED -> image.saveImage(outputResourcesDirectories)
                    ChangeType.REMOVED -> image.removeImage(outputResourcesDirectories)
                }
            }

        // Generate image sets
        inputDirectory.files
            .map(::ImageProps)
            .groupBy(ImageProps::name)
            .map { (name, files) -> ImageSet(name, files) }
            .forEach { catalog ->
                catalog.saveImageSet(outputResourcesDirectories)
            }

        // Generate code
        inputDirectory.files
            .takeIf { files -> files.isNotEmpty() }
            ?.map(::ImageProps)
            ?.distinctBy(ImageProps::name)
            ?.let(::buildImages)
            ?: buildEmptyImages()
    }

    private fun buildImages(imageProps: List<ImageProps>) {
        val builder = ImagesTypeSpecsBuilder(settings, outputSourcesDirectories.keys)
        imageProps.forEach { builder.appendImage(it) }
        outputSourcesDirectories.forEach { it.value.deleteFilesInDirectory() }
        builder.save(outputSourcesDirectories)
    }

    private fun buildEmptyImages() {
        val hasCommon = KotlinPlatform.Common in outputSourcesDirectories.keys
        outputSourcesDirectories.forEach { (platform, directory) ->
            val imagesObjectTypeSpec = EmptyImagesObject(settings.outputClassName, platform, hasCommon)
            val imagesObjectFileSpec = ImagesObjectFile(settings.outputPackageName, imagesObjectTypeSpec, platform)
            directory.deleteFilesInDirectory()
            imagesObjectFileSpec.saveToDirectory(directory)
        }
    }
}

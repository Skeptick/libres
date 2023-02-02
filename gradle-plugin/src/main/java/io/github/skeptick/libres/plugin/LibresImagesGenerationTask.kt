package io.github.skeptick.libres.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*
import org.gradle.work.ChangeType
import org.gradle.work.Incremental
import org.gradle.work.InputChanges
import io.github.skeptick.libres.plugin.common.declarations.saveToDirectory
import io.github.skeptick.libres.plugin.common.extensions.deleteFilesInDirectory
import io.github.skeptick.libres.plugin.common.project.appleBundleName
import io.github.skeptick.libres.plugin.images.ImagesTypeSpecsBuilder
import io.github.skeptick.libres.plugin.images.declarations.EmptyImagesObject
import io.github.skeptick.libres.plugin.images.declarations.ImagesObjectFile
import io.github.skeptick.libres.plugin.images.models.ImageProps
import io.github.skeptick.libres.plugin.images.processing.removeImage
import io.github.skeptick.libres.plugin.images.processing.saveImage
import java.io.File

@CacheableTask
abstract class LibresImagesGenerationTask : DefaultTask() {

    @get:Input
    internal abstract var outputPackageName: String

    @get:Input
    internal abstract var outputClassName: String

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
        inputChanges.getFileChanges(inputDirectory).forEach { change ->
            @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
            when (change.changeType) {
                ChangeType.REMOVED -> ImageProps(change.file).removeImage(outputResourcesDirectories)
                ChangeType.MODIFIED, ChangeType.ADDED -> ImageProps(change.file).saveImage(outputResourcesDirectories)
            }
        }

        outputSourcesDirectories.forEach { it.value.deleteFilesInDirectory() }
        inputDirectory.files
            .takeIf { files -> files.isNotEmpty() }
            ?.map { file -> ImageProps(file) }
            ?.let { imageProps -> buildImages(imageProps) }
            ?: buildEmptyImages()
    }

    private fun buildImages(imageProps: List<ImageProps>) {
        val builder = ImagesTypeSpecsBuilder(
            outputPackageName = outputPackageName,
            outputClassName = outputClassName,
            platforms = outputSourcesDirectories.keys,
            iosBundleName = project.appleBundleName
        )

        imageProps.forEach { builder.appendImage(it) }
        builder.save(outputSourcesDirectories)
    }

    private fun buildEmptyImages() {
        val hasCommon = KotlinPlatform.Common in outputSourcesDirectories.keys
        outputSourcesDirectories.forEach { (platform, directory) ->
            val imagesObjectTypeSpec = EmptyImagesObject(outputClassName, platform, hasCommon)
            val imagesObjectFileSpec = ImagesObjectFile(outputPackageName, imagesObjectTypeSpec, platform)
            imagesObjectFileSpec.saveToDirectory(directory)
        }
    }

}
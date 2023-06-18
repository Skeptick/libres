package io.github.skeptick.libres.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*
import io.github.skeptick.libres.plugin.common.declarations.saveToDirectory
import io.github.skeptick.libres.plugin.common.extensions.deleteFilesInDirectory
import io.github.skeptick.libres.plugin.strings.*
import io.github.skeptick.libres.plugin.strings.declarations.*
import io.github.skeptick.libres.plugin.strings.models.*
import java.io.File

@CacheableTask
abstract class LibresStringGenerationTask : DefaultTask() {

    @get:Input
    internal abstract var settings: StringsSettings

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    internal abstract var inputDirectories: List<FileCollection>

    @get:OutputDirectory
    internal abstract var outputDirectory: File

    @TaskAction
    fun apply() {
        inputDirectories
            .mapNotNull { fileCollection ->
                fileCollection.files.takeIf { files -> files.isNotEmpty() }
            }
            .let { listFiles ->
                parseStringResources(listFiles, settings.baseLocaleLanguageCode)
            }
            .takeIf { resources -> resources.getValue(settings.baseLocaleLanguageCode).isNotEmpty() }
            ?.let { resources -> buildResources(resources) }
            ?: buildEmptyResources()
    }

    private fun buildResources(resources: Map<LanguageCode, List<TextResource>>) {
        val builder = StringTypeSpecsBuilder(settings, resources.keys)
        val resourceByLanguageCodes = resources.mapValues { it.value.associateBy(TextResource::name) }

        resources.getValue(settings.baseLocaleLanguageCode).forEach { baseResource ->
            builder.appendResource(
                baseResource = baseResource,
                localizedResources = resources.mapValues {
                    resourceByLanguageCodes[it.key]?.get(baseResource.name)
                }
            )
        }

        outputDirectory.deleteFilesInDirectory()
        builder.save(outputDirectory)
    }

    private fun buildEmptyResources() {
        val stringObjectTypeSpec = EmptyStringObject(settings.outputClassName)
        val stringsFileSpec = StringsObjectFile(settings.outputPackageName, stringObjectTypeSpec)
        outputDirectory.deleteFilesInDirectory()
        stringsFileSpec.saveToDirectory(outputDirectory)
    }

}
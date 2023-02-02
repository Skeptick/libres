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
    internal abstract var outputPackageName: String

    @get:Input
    internal abstract var outputClassName: String

    @get:Input
    internal abstract var generateNamedArguments: Boolean

    @get:Input
    internal abstract var baseLocaleLanguageCode: String

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    internal abstract var inputDirectory: FileCollection

    @get:OutputDirectory
    internal abstract var outputDirectory: File

    @TaskAction
    fun apply() {
        outputDirectory.deleteFilesInDirectory()
        inputDirectory.files
            .takeIf { files -> files.isNotEmpty() }
            ?.let { files -> parseStringResources(files, baseLocaleLanguageCode) }
            ?.takeIf { resources -> resources.getValue(baseLocaleLanguageCode).isNotEmpty() }
            ?.let { resources -> buildResources(resources) }
            ?: buildEmptyResources()
    }

    private fun buildResources(resources: Map<LanguageCode, List<TextResource>>) {
        val builder = StringTypeSpecsBuilder(
            outputPackageName = outputPackageName,
            outputClassName = outputClassName,
            generateNamedArguments = generateNamedArguments,
            baseLanguageCode = baseLocaleLanguageCode,
            languageCodes = resources.keys
        )

        val resourceByLanguageCodes = resources.mapValues { it.value.associateBy(TextResource::name) }
        resources.getValue(baseLocaleLanguageCode).forEach { baseResource ->
            builder.appendResource(
                baseResource = baseResource,
                localizedResources = resources.mapValues {
                    resourceByLanguageCodes[it.key]?.get(baseResource.name)
                }
            )
        }

        builder.save(outputDirectory)
    }

    private fun buildEmptyResources() {
        val stringObjectTypeSpec = EmptyStringObject(outputClassName)
        val stringsFileSpec = StringsObjectFile(outputPackageName, stringObjectTypeSpec)
        stringsFileSpec.saveToDirectory(outputDirectory)
    }

}
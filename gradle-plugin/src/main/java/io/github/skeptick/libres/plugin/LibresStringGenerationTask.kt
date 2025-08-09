package io.github.skeptick.libres.plugin

import io.github.skeptick.libres.plugin.common.declarations.saveTo
import io.github.skeptick.libres.plugin.common.extensions.deleteFiles
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import io.github.skeptick.libres.plugin.strings.*
import io.github.skeptick.libres.plugin.strings.declarations.*
import io.github.skeptick.libres.plugin.strings.models.*
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property

@CacheableTask
abstract class LibresStringGenerationTask : DefaultTask() {

    @get:Input
    internal abstract val outputPackageName: Property<String>

    @get:Input
    internal abstract val outputClassName: Property<String>

    @get:Input
    internal abstract val baseLocaleLanguageCode: Property<String>

    @get:Input
    internal abstract val generateNamedArguments: Property<Boolean>

    @get:Input
    internal abstract val camelCaseNamesForAppleFramework: Property<Boolean>

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:IgnoreEmptyDirectories
    internal abstract val inputFiles: ConfigurableFileCollection

    @get:OutputDirectory
    internal abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun apply() {
        if (inputFiles.isEmpty) {
            buildEmptyResources()
        } else {
            val stringResources = parseStringResources(
                inputFiles = inputFiles.files,
                baseLocaleLanguageCode = baseLocaleLanguageCode.get()
            )
            if (stringResources.getValue(baseLocaleLanguageCode.get()).isNotEmpty()) {
                buildResources(stringResources)
            } else {
                buildEmptyResources()
            }
        }
    }

    private fun buildResources(resources: Map<LanguageCode, List<TextResource>>) {
        val builder = StringTypeSpecsBuilder(
            outputPackageName = outputPackageName.get(),
            outputClassName = outputClassName.get(),
            languageCodes = resources.keys,
            baseLanguageCode = baseLocaleLanguageCode.get(),
            generateNamedArguments = generateNamedArguments.get(),
            camelCaseForApple = camelCaseNamesForAppleFramework.get()
        )
        val resourceByLanguageCodes = resources.mapValues { it.value.associateBy(TextResource::name) }

        resources.getValue(baseLocaleLanguageCode.get()).forEach { baseResource ->
            builder.appendResource(
                baseResource = baseResource,
                localizedResources = resources.mapValues {
                    resourceByLanguageCodes[it.key]?.get(baseResource.name)
                }
            )
        }

        outputDirectory.get().let { outputDir ->
            outputDir.deleteFiles()
            builder.save(outputDir)
        }
    }

    private fun buildEmptyResources() {
        val stringObjectTypeSpec = EmptyStringObject(outputClassName.get())
        val stringsFileSpec = StringsObjectFile(outputPackageName.get(), stringObjectTypeSpec)

        outputDirectory.get().let { outputDir ->
            outputDir.deleteFiles()
            stringsFileSpec.saveTo(outputDir)
        }
    }

}
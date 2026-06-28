package io.github.skeptick.libres.plugin

import io.github.skeptick.libres.plugin.common.saveTo
import io.github.skeptick.libres.plugin.declarations.ResourcesObject
import io.github.skeptick.libres.plugin.declarations.StringsCommonObject
import io.github.skeptick.libres.plugin.declarations.StringsEmptyObject
import io.github.skeptick.libres.plugin.declarations.StringsFormatClasses
import io.github.skeptick.libres.plugin.declarations.StringsInterface
import io.github.skeptick.libres.plugin.declarations.StringsLocalizedObject
import io.github.skeptick.libres.plugin.models.LocaleTag
import io.github.skeptick.libres.plugin.models.ResourcesSettings
import io.github.skeptick.libres.plugin.models.TextResource
import io.github.skeptick.libres.plugin.models.parseLocaleTag
import io.github.skeptick.libres.plugin.parsing.parseStrings
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.IgnoreEmptyDirectories
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

@CacheableTask
abstract class LibresResourcesGenerationTask @Inject constructor(objects: ObjectFactory) : DefaultTask() {

    @get:Nested
    internal val settings: ResourcesSettingsInput = objects.newInstance(ResourcesSettingsInput::class.java)

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:IgnoreEmptyDirectories
    internal abstract val inputFiles: ConfigurableFileCollection

    @get:OutputDirectory
    internal abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun apply() {
        outputDirectory.get().asFile.deleteRecursively()

        val resourcesSettings = settings.toSettings()

        if (inputFiles.isEmpty) {
            buildEmptyResources(resourcesSettings)
        } else {
            val localeTags = inputFiles.files.map(File::parseLocaleTag).toSet()
            val stringResources = parseStrings(inputFiles.files, resourcesSettings.baseLocaleTag)
            if (stringResources.isNotEmpty()) {
                buildResources(settings = resourcesSettings, resources = stringResources, localeTags = localeTags)
            } else {
                buildEmptyResources(resourcesSettings)
            }
        }
    }

    private fun buildEmptyResources(settings: ResourcesSettings) {
        outputDirectory.get().packageDirectory(settings).let { outputDir ->
            outputDir.asFile.mkdirs()
            ResourcesObject(settings).saveTo(outputDir)
            StringsEmptyObject(settings).saveTo(outputDir.dir("strings"))
        }
    }

    private fun buildResources(settings: ResourcesSettings, resources: List<TextResource>, localeTags: Set<LocaleTag>) {
        val stringsInterface = StringsInterface(settings, resources)
        val stringsFormatClasses = StringsFormatClasses(settings, resources)
        val stringsLocalizedObjects = localeTags.map { localeTag -> StringsLocalizedObject(settings, localeTag, resources) }
        val stringsCommonObject = StringsCommonObject(settings, localeTags, resources)

        outputDirectory.get().packageDirectory(settings).let { outputDir ->
            outputDir.asFile.mkdirs()
            ResourcesObject(settings).saveTo(outputDir)
            outputDir.dir("strings").let { stringsOutputDir ->
                stringsInterface.saveTo(stringsOutputDir)
                stringsFormatClasses.saveTo(stringsOutputDir)
                stringsLocalizedObjects.forEach { it.saveTo(stringsOutputDir) }
                stringsCommonObject.saveTo(stringsOutputDir)
            }
        }
    }

    private fun Directory.packageDirectory(settings: ResourcesSettings): Directory {
        return dir(settings.packageName.replace('.', '/'))
    }

}
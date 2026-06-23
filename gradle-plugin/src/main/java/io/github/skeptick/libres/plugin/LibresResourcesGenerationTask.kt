package io.github.skeptick.libres.plugin

import io.github.skeptick.libres.plugin.common.saveTo
import io.github.skeptick.libres.plugin.declarations.ResourcesObject
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

@CacheableTask
abstract class LibresResourcesGenerationTask @Inject constructor(objects: ObjectFactory) : DefaultTask() {

    @get:Nested
    internal val settings: ResourcesSettingsInput = objects.newInstance(ResourcesSettingsInput::class.java)

    @get:OutputDirectory
    internal abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun apply() {
        val resourcesObject = ResourcesObject(settings.toSettings())

        outputDirectory.get().let { outputDir ->
            outputDir.asFile.listFiles().forEach { if (it.isFile) it.delete() }
            resourcesObject.saveTo(outputDir)
        }
    }

}
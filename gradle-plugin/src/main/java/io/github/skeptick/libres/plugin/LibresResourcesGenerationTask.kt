package io.github.skeptick.libres.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import io.github.skeptick.libres.plugin.common.declarations.ResourcesFile
import io.github.skeptick.libres.plugin.common.declarations.ResourcesObject
import io.github.skeptick.libres.plugin.common.declarations.saveToDirectory
import io.github.skeptick.libres.plugin.common.extensions.deleteFilesInDirectory
import java.io.File

@CacheableTask
abstract class LibresResourcesGenerationTask : DefaultTask() {

    @get:Input
    internal abstract var settings: ResourcesSettings

    @get:OutputDirectory
    internal abstract var outputDirectory: File

    @TaskAction
    fun apply() {
        val className = settings.outputClassName
        val packageName = settings.outputPackageName
        val stringsPackageName = settings.stringsPackageName
        val imagesPackageName = settings.imagesPackageName

        outputDirectory.deleteFilesInDirectory()
        val resourcesObjectTypeSpec = ResourcesObject(className, stringsPackageName, imagesPackageName)
        val resourcesFileSpec = ResourcesFile(packageName, className, resourcesObjectTypeSpec)
        resourcesFileSpec.saveToDirectory(outputDirectory)
    }

}
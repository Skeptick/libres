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
    internal abstract var outputPackageName: String

    @get:Input
    internal abstract var outputClassName: String

    @get:Input
    internal abstract var stringsPackageName: String

    @get:Input
    internal abstract var imagesPackageName: String

    @get:OutputDirectory
    internal abstract var outputDirectory: File

    @TaskAction
    fun apply() {
        outputDirectory.deleteFilesInDirectory()
        val resourcesObjectTypeSpec = ResourcesObject(outputClassName, stringsPackageName, imagesPackageName)
        val resourcesFileSpec = ResourcesFile(outputPackageName, outputClassName, resourcesObjectTypeSpec)
        resourcesFileSpec.saveToDirectory(outputDirectory)
    }

}
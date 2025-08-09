package io.github.skeptick.libres.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import io.github.skeptick.libres.plugin.common.declarations.ResourcesFile
import io.github.skeptick.libres.plugin.common.declarations.ResourcesObject
import io.github.skeptick.libres.plugin.common.declarations.saveTo
import io.github.skeptick.libres.plugin.common.extensions.deleteFiles
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property

@CacheableTask
abstract class LibresResourcesGenerationTask : DefaultTask() {

    @get:Input
    internal abstract val outputPackageName: Property<String>

    @get:Input
    internal abstract val outputClassName: Property<String>

    @get:Input
    internal abstract val stringsOutputPackageName: Property<String>

    @get:OutputDirectory
    internal abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun apply() {
        val className = outputClassName.get()
        val packageName = outputPackageName.get()
        val stringsPackageName = stringsOutputPackageName.get()

        val resourcesObjectTypeSpec = ResourcesObject(className, stringsPackageName)
        val resourcesFileSpec = ResourcesFile(packageName, className, resourcesObjectTypeSpec)

        outputDirectory.get().let { outputDir ->
            outputDir.deleteFiles()
            resourcesFileSpec.saveTo(outputDir)
        }
    }

}
package io.github.skeptick.libres.plugin

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import io.github.skeptick.libres.VERSION
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSet
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.sources.DefaultKotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

class ResourcesPlugin : Plugin<Project> {

    private var isAndroid = false

    private var isKotlin = false

    private lateinit var pluginExtension: ResourcesPluginExtension

    override fun apply(project: Project) {
        pluginExtension = project.extensions.create("libres", ResourcesPluginExtension::class.java)

        val setupTasks: (afterAndroid: Boolean) -> Unit = { afterAndroid ->
            project.afterEvaluate {
                if (!isKotlin || (isAndroid && !afterAndroid)) return@afterEvaluate
                it.setDependencies()
                it.registerGeneratorsTasks()
            }
        }

        project.plugins.withId("com.android.base") {
            isAndroid = true
            setupTasks(true)
        }

        project.plugins.withType(KotlinBasePlugin::class.java) {
            isKotlin = true
            setupTasks(false)
        }
    }

    private fun Project.setDependencies() {
        if (project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
            val sourceSets = project.extensions.getByType(KotlinMultiplatformExtension::class.java).sourceSets
            val sourceSet = sourceSets.getByName("commonMain") as DefaultKotlinSourceSet
            project.configurations.getByName(sourceSet.apiConfigurationName).dependencies.add(
                project.dependencies.create("io.github.skeptick.libres:libres:$VERSION")
            )
        } else {
            project.configurations.getByName("api").dependencies.add(
                project.dependencies.create("io.github.skeptick.libres:libres:$VERSION")
            )
        }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    private fun Project.registerGeneratorsTasks() {
        val kotlinExtension = project.extensions.getByType(KotlinProjectExtension::class.java)
        val kotlinMultiplatformExtension = project.extensions.findByType(KotlinMultiplatformExtension::class.java)
        val kotlinAndroidExtension = kotlinMultiplatformExtension?.extensions?.findByType(KotlinMultiplatformAndroidLibraryExtension::class.java)
        val androidExtension = project.extensions.findByType(CommonExtension::class.java)

        val libresSourceSet = when {
            kotlinMultiplatformExtension != null -> {
                val commonSourceSet = kotlinMultiplatformExtension.sourceSets.getByName(KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME)
                LibresSourceSet(
                    name = commonSourceSet.name,
                    registerGeneratedSources = { commonSourceSet.generatedKotlin.srcDir(it) }
                )
            }
            androidExtension != null -> {
                val androidMainSourceSet = androidExtension.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
                LibresSourceSet(
                    name = androidMainSourceSet.name,
                    registerGeneratedSources = { androidMainSourceSet.kotlin.srcDir(it) }
                )
            }
            else -> {
                val target = kotlinExtension.targets.firstOrNull() ?: return
                val defaultSourceSet = target.compilations.firstOrNull { it.isMainCompilation }?.defaultSourceSet ?: return
                LibresSourceSet(
                    name = defaultSourceSet.name,
                    registerGeneratedSources = { defaultSourceSet.generatedKotlin.srcDir(it) }
                )
            }
        }

        val outputPackageName = pluginExtension.generatedClassPackageNameProp.convention(
            androidExtension?.namespace ?: kotlinAndroidExtension?.namespace ?: DEFAULT_RESOURCES_PACKAGE_NAME
        )

        val resourcesTask = tasks.register("libresGenerateResources", LibresResourcesGenerationTask::class.java) { task ->
            val stringsInputDirectory = libresSourceSet.inputDirectory.dir("strings")
            task.group = TASK_GROUP
            task.settings.configure(outputPackageName)
            task.inputFiles.setFrom(stringsInputDirectory.asFileTree.matching { it.include("**/*.xml") })
            task.outputDirectory.set(libresSourceSet.outputDirectory)
        }

        pluginExtension.finalizeValuesOnRead()
        libresSourceSet.registerGeneratedSources(resourcesTask)
        tasks.withType(KotlinCompilationTask::class.java).configureEach { it.dependsOn(resourcesTask) }
    }

    private fun ResourcesSettingsInput.configure(outputPackageName: Provider<String>) {
        resourcesName.set(pluginExtension.generatedClassNameProp)
        packageName.set(outputPackageName)
        generateNamedArguments.set(pluginExtension.generateNamedArgumentsProp)
        generateInternalResourceClasses.set(pluginExtension.generateInternalResourceClassesProp)
        camelCaseForApple.set(pluginExtension.camelCaseNamesForAppleFrameworkProp)
        baseLocaleTag.set(pluginExtension.baseLocaleTagProp)
    }

    companion object {

        private const val TASK_GROUP = "libres"

        private const val DEFAULT_RESOURCES_PACKAGE_NAME = "libres.resources"
    }

}
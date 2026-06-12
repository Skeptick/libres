package io.github.skeptick.libres.plugin

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import io.github.skeptick.libres.VERSION
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskProvider
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.sources.DefaultKotlinSourceSet

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

    private fun Project.registerGeneratorsTasks() {
        val kotlinExtension = project.extensions.getByType(KotlinProjectExtension::class.java)
        val kotlinMultiplatformExtension = project.extensions.findByType(KotlinMultiplatformExtension::class.java)
        val kotlinAndroidExtension = kotlinMultiplatformExtension?.extensions?.findByType(KotlinMultiplatformAndroidLibraryExtension::class.java)
        val androidExtension = project.extensions.findByType(BaseExtension::class.java)

        val inputDirectory: Provider<Directory>
        val baseOutputDirectory: Provider<Directory>
        val sourceSetRegistrator: (TaskProvider<*>) -> Unit

        when {
            kotlinMultiplatformExtension != null -> {
                val commonSourceSet = kotlinMultiplatformExtension.sourceSets.getByName(KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME)
                inputDirectory = layout.projectDirectory.dir(provider { "src/${commonSourceSet.name}/libres" })
                baseOutputDirectory = layout.buildDirectory.dir("generated/libres/common/src")
                sourceSetRegistrator = { commonSourceSet.kotlin.srcDir(it) }
            }
            androidExtension != null -> {
                val androidMainSourceSet = androidExtension.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
                inputDirectory = layout.projectDirectory.dir(provider { "src/${androidMainSourceSet.name}/libres" })
                baseOutputDirectory = layout.buildDirectory.dir("generated/libres/android/src")
                sourceSetRegistrator = { androidMainSourceSet.kotlin.srcDir(it) }
            }
            else -> {
                val target = kotlinExtension.targets.firstOrNull { it.platform != null } ?: return
                val defaultSourceSet = target.compilations.firstOrNull { it.isMainCompilation }?.defaultSourceSet ?: return
                inputDirectory = layout.projectDirectory.dir(provider { "src/${defaultSourceSet.name}/libres" })
                baseOutputDirectory = layout.buildDirectory.dir("generated/libres/${target.platform?.name?.lowercase() ?: "default"}/src")
                sourceSetRegistrator = { defaultSourceSet.kotlin.srcDir(it) }
            }
        }

        val outputPackageName = pluginExtension.generatedClassPackageNameProp.convention(
            androidExtension?.namespace ?: kotlinAndroidExtension?.namespace ?: DEFAULT_RESOURCES_PACKAGE_NAME
        )
        val stringsOutputPackageName = outputPackageName.map { packageName -> "$packageName.strings" }

        val stringsTask = tasks.register(GENERATE_STRINGS_TASK_NAME, LibresStringGenerationTask::class.java) { task ->
            val stringsInputDirectory = inputDirectory.map { it.dir("strings") }
            task.group = TASK_GROUP
            task.outputPackageName.set(stringsOutputPackageName)
            task.outputClassName.set(pluginExtension.generatedClassNameProp)
            task.baseLocaleLanguageCode.set(pluginExtension.baseLocaleLanguageCodeProp)
            task.generateNamedArguments.set(pluginExtension.generateNamedArgumentsProp)
            task.camelCaseNamesForAppleFramework.set(
                pluginExtension.camelCaseNamesForAppleFrameworkProp.map { property ->
                    property && kotlinExtension.targets.any { it.platform == KotlinPlatform.Apple }
                }
            )
            task.inputFiles.setFrom(
                stringsInputDirectory.map { directory ->
                    directory.asFileTree.matching { pattern -> pattern.include("**/*.xml") }
                }
            )
            task.outputDirectory.set(baseOutputDirectory.map { it.dir("strings") })
        }

        val resourcesTask = tasks.register(GENERATE_RESOURCES_TASK_NAME, LibresResourcesGenerationTask::class.java) { task ->
            task.group = TASK_GROUP
            task.outputPackageName.set(outputPackageName)
            task.outputClassName.set(pluginExtension.generatedClassNameProp)
            task.stringsOutputPackageName.set(stringsOutputPackageName)
            task.outputDirectory.set(baseOutputDirectory.map { it.dir("resources") })
            task.dependsOn(stringsTask)
        }

        pluginExtension.finalizeValuesOnRead()
        sourceSetRegistrator(stringsTask)
        sourceSetRegistrator(resourcesTask)
    }

    companion object {

        private const val DEFAULT_RESOURCES_PACKAGE_NAME = "libres.resources"

        private const val LIBRARY_PACKAGE_NAME = "io.github.skeptick.libres"

        internal const val STRINGS_PACKAGE_NAME = "$LIBRARY_PACKAGE_NAME.strings"

        private const val TASK_GROUP = "libres"

        const val GENERATE_RESOURCES_TASK_NAME = "libresGenerateResources"

        const val GENERATE_STRINGS_TASK_NAME = "libresGenerateStrings"

    }

}
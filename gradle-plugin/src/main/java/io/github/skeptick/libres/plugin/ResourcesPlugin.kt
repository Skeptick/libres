package io.github.skeptick.libres.plugin

import io.github.skeptick.libres.VERSION
import io.github.skeptick.libres.plugin.common.project.*
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.api.DefaultAndroidSourceFile
import com.android.build.gradle.tasks.GenerateResValues
import com.android.ide.common.symbols.getPackageNameFromManifest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.SourceSet
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.KotlinCocoapodsPlugin
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.sources.DefaultKotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.PodspecTask
import java.io.File
import io.github.skeptick.libres.plugin.SourceSet as PluginSourceSet

class ResourcesPlugin : Plugin<Project> {

    private var isAndroid = false
    private var isKotlin = false

    private lateinit var pluginExtension: ResourcesPluginExtension
    private lateinit var inputDirectory: File
    private lateinit var mainSourceSet: PluginSourceSet
    private val allSourceSets = mutableListOf<PluginSourceSet>()
    private var outputPackageName: String? = null

    override fun apply(project: Project) {
        pluginExtension = project.extensions.create("libres", ResourcesPluginExtension::class.java)

        val setupTasks: (afterAndroid: Boolean) -> Unit = { afterAndroid ->
            project.afterEvaluate {
                if (!isKotlin || (isAndroid && !afterAndroid)) return@afterEvaluate
                it.fetchSourceSets()
                it.setDependencies()
                it.registerSetupPodspecExportsTask()
                it.registerGeneratorsTasks()
            }
        }

        val androidPluginHandler = { _: Plugin<*> ->
            isAndroid = true
            setupTasks(true)
        }

        project.plugins.withId("com.android.application", androidPluginHandler)
        project.plugins.withId("com.android.library", androidPluginHandler)
        project.plugins.withId("com.android.instantapp", androidPluginHandler)
        project.plugins.withId("com.android.feature", androidPluginHandler)
        project.plugins.withId("com.android.dynamic-feature", androidPluginHandler)

        val kotlinPluginHandler = { _: Plugin<*> ->
            isKotlin = true
            setupTasks(false)
        }

        project.plugins.withId("org.jetbrains.kotlin.multiplatform", kotlinPluginHandler)
        project.plugins.withId("org.jetbrains.kotlin.android", kotlinPluginHandler)
        project.plugins.withId("org.jetbrains.kotlin.jvm", kotlinPluginHandler)
        project.plugins.withId("org.jetbrains.kotlin.js", kotlinPluginHandler)
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

    private fun Project.fetchSourceSets() {
        val outputDirectory = File(project.buildDir, "generated/libres")
        val kotlinExtension = project.extensions.getByType(KotlinProjectExtension::class.java)
        val commonSourceSet = kotlinExtension.sourceSets.findByName(KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME)
        val androidExtension = project.extensions.findByName("android") as BaseExtension?
        outputPackageName = androidExtension?.packageName

        when {
            commonSourceSet != null -> {
                inputDirectory = commonSourceSet.resources.sourceDirectories.first().resolveSibling("libres")
                mainSourceSet = commonSourceSet.createKotlinSourceSet(outputDirectory, KotlinPlatform.Common)
                allSourceSets += kotlinExtension.targets.takeKotlinSourceSet(outputDirectory, commonSourceSet)
                if (androidExtension != null) allSourceSets += androidExtension.findAndroidSourceSet(true, outputDirectory, kotlinExtension)
                allSourceSets += mainSourceSet
            }
            androidExtension != null -> {
                val androidMainSourceSet = androidExtension.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
                inputDirectory = androidMainSourceSet.resources.srcDirs.first().resolveSibling("libres")
                mainSourceSet = androidExtension.findAndroidSourceSet(false, outputDirectory, kotlinExtension)
                allSourceSets += mainSourceSet
            }
            else -> {
                val target = kotlinExtension.targets.firstOrNull { it.platform != null } ?: return
                val defaultSourceSet = target.compilations.firstOrNull { it.isMainCompilation }?.defaultSourceSet ?: return
                mainSourceSet = defaultSourceSet.createKotlinSourceSet(outputDirectory, target.platform!!)
                inputDirectory = defaultSourceSet.resources.sourceDirectories.first().resolveSibling("libres")
                allSourceSets += mainSourceSet
            }
        }
    }

    private fun Project.registerGeneratorsTasks() {
        val stringsInputDirectory = File(inputDirectory, "strings")
        val imagesInputDirectory = File(inputDirectory, "images")
        val nightImagesInputDirectory = File(inputDirectory, "images-night")
        val stringsOutputPackageName = listOfNotNull(outputPackageName, "strings").joinToString(".")
        val imagesOutputPackageName = listOfNotNull(outputPackageName, "images").joinToString(".")

        val stringsTask = project.tasks.register(GENERATE_STRINGS_TASK_NAME, LibresStringGenerationTask::class.java) { task ->
            task.group = TASK_GROUP
            task.settings = StringsSettings(
                outputPackageName = stringsOutputPackageName,
                outputClassName = pluginExtension.generatedClassName,
                generateNamedArguments = pluginExtension.generateNamedArguments,
                baseLocaleLanguageCode = pluginExtension.baseLocaleLanguageCode,
                camelCaseForApple = pluginExtension.camelCaseNamesForAppleFramework && allSourceSets.any { it.platform == KotlinPlatform.Apple }
            )
            task.inputDirectory = fileTree(stringsInputDirectory) { config ->
                config.include { element ->
                    !element.isDirectory && element.file.extension.lowercase() in STRINGS_EXTENSIONS
                }
            }
            task.outputDirectory = mainSourceSet.sourcesDir.toOutputDirectory(stringsOutputPackageName)
        }

        val imagesTask = project.tasks.register(GENERATE_IMAGES_TASK_NAME, LibresImagesGenerationTask::class.java) { task ->
            task.group = TASK_GROUP
            task.settings = ImagesSettings(
                outputPackageName = imagesOutputPackageName,
                outputClassName = pluginExtension.generatedClassName,
                camelCaseForApple = pluginExtension.camelCaseNamesForAppleFramework,
                appleBundleName = project.appleBundleName
            )
            task.inputDirectory = fileTree(imagesInputDirectory) { config ->
                config.include { element ->
                    !element.isDirectory && element.file.extension.lowercase() in IMAGES_EXTENSIONS
                }
            }
            task.nightInputDirectory = fileTree(nightImagesInputDirectory) { config ->
                config.include { element ->
                    !element.isDirectory && element.file.extension.lowercase() in IMAGES_EXTENSIONS
                }
            }
            task.outputSourcesDirectories = allSourceSets.associateBy(PluginSourceSet::platform) {
                it.sourcesDir.toOutputDirectory(imagesOutputPackageName)
            }
            task.outputResourcesDirectories = allSourceSets.associateBy(PluginSourceSet::platform) {
                when (it.platform) {
                    KotlinPlatform.Apple -> File(it.resourcesDir, "images/${project.appleBundleName}.xcassets")
                    KotlinPlatform.Js, KotlinPlatform.Jvm -> File(it.resourcesDir, "images")
                    else -> it.resourcesDir
                }
            }
        }

        val resourcesTask = project.tasks.register(GENERATE_RESOURCES_TASK_NAME, LibresResourcesGenerationTask::class.java) { task ->
            task.group = TASK_GROUP
            task.settings = ResourcesSettings(
                outputPackageName = outputPackageName.orEmpty(),
                outputClassName = pluginExtension.generatedClassName,
                stringsPackageName = stringsOutputPackageName,
                imagesPackageName = imagesOutputPackageName
            )
            task.outputDirectory = mainSourceSet.sourcesDir.toOutputDirectory(outputPackageName.orEmpty())
            task.dependsOn(stringsTask)
            task.dependsOn(imagesTask)
        }

        project.tasks.apply {
            getByName("build").dependsOn(resourcesTask)
            withType(KotlinCompile::class.java).configureEach { it.dependsOn(resourcesTask) }
            if (isAndroid) withType(GenerateResValues::class.java).configureEach { it.dependsOn(imagesTask) }
            findByPath("desktopProcessResources")?.dependsOn(imagesTask)
            findByPath("desktopSourcesJar")?.dependsOn(resourcesTask)
            findByPath("jsProcessResources")?.dependsOn(imagesTask)
        }
    }

    private fun Project.registerSetupPodspecExportsTask() {
        if (!plugins.hasPlugin(KotlinCocoapodsPlugin::class.java)) return
        val multiplatformExtension = extensions.findByType(KotlinMultiplatformExtension::class.java) ?: return

        afterEvaluate {
            val setupPodspecTask = tasks.register(SETUP_PODSPEC_TASK_NAME) { task ->
                task.group = TASK_GROUP
                task.doLast {
                    multiplatformExtension.cocoapodsExtensionOrNull?.let {
                        val exports = getCocoapodsExports(multiplatformExtension)
                        createAssetsSymLinks(project, exports)
                        it.extraSpecAttributes["resource_bundles"] = (exports + project).appleImageBundles(project)
                    }
                }
            }

            tasks.withType(PodspecTask::class.java).all { compileTask ->
                compileTask.dependsOn(setupPodspecTask)
            }
        }
    }

    private fun Project.getCocoapodsExports(multiplatformExtension: KotlinMultiplatformExtension): List<Project> {
        val configName = multiplatformExtension.cocoapodsExportConfigurationName ?: return emptyList()
        val exports = configurations.getByName(configName).dependencies.filterIsInstance<ProjectDependency>().map { it.dependencyProject }
        return exports.filter { it.plugins.hasPlugin(ResourcesPlugin::class.java) }
    }

    companion object {

        private const val LIBRARY_PACKAGE_NAME = "io.github.skeptick.libres"
        internal const val STRINGS_PACKAGE_NAME = "$LIBRARY_PACKAGE_NAME.strings"
        internal const val IMAGES_PACKAGE_NAME = "$LIBRARY_PACKAGE_NAME.images"

        private const val TASK_GROUP = "libres"
        const val GENERATE_RESOURCES_TASK_NAME = "libresGenerateResources"
        const val GENERATE_STRINGS_TASK_NAME = "libresGenerateStrings"
        const val GENERATE_IMAGES_TASK_NAME = "libresGenerateImages"
        const val SETUP_PODSPEC_TASK_NAME = "libresSetupPodspecExports"

        private val STRINGS_EXTENSIONS = listOf("xml")
        private val IMAGES_EXTENSIONS = listOf("png", "jpg", "jpeg", "webp", "svg")

        private val BaseExtension.packageName: String?
            get() = namespace ?: run {
                val sourceSet = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
                val manifest = sourceSet.manifest as? DefaultAndroidSourceFile ?: return@run null
                manifest.srcFile.takeIf(File::exists)?.let(::getPackageNameFromManifest)
            }

        private val KotlinMultiplatformExtension?.cocoapodsExtensionOrNull: CocoapodsExtension?
            get() = (this as? ExtensionAware)?.extensions?.findByType(CocoapodsExtension::class.java)

        private val KotlinMultiplatformExtension.cocoapodsExportConfigurationName: String?
            get() = targets
                .filterIsInstance<KotlinNativeTarget>().firstOrNull { it.konanTarget.family.isAppleFamily }
                ?.binaries?.filterIsInstance<Framework>()?.firstOrNull()?.exportConfigurationName

        private fun File.toOutputDirectory(packageName: String) =
            File(absolutePath, packageName.replace('.', File.separatorChar))

    }
}

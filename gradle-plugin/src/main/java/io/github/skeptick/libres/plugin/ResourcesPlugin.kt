package io.github.skeptick.libres.plugin

import io.github.skeptick.libres.VERSION
import io.github.skeptick.libres.plugin.common.project.*
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.api.DefaultAndroidSourceFile
import com.android.build.gradle.tasks.GenerateResValues
import com.android.ide.common.symbols.getPackageNameFromManifest
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.SourceSet
import org.gradle.jvm.tasks.Jar
import org.gradle.language.jvm.tasks.ProcessResources
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.KotlinCocoapodsPlugin
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.sources.DefaultKotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
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
                it.registerGeneratorsTasks()
                it.registerSetupIosExportTasks()
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
                allSourceSets += kotlinExtension.targets.takeKotlinSourceSet(outputDirectory)
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

    @Suppress("UnstableApiUsage")
    private fun Project.registerGeneratorsTasks() {
        val stringsInputDirectory = File(inputDirectory, "strings")
        val imagesInputDirectory = File(inputDirectory, "images")
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
            task.outputSourcesDirectories = allSourceSets.associateBy(PluginSourceSet::platform) {
                it.sourcesDir.toOutputDirectory(imagesOutputPackageName)
            }
            task.outputResourcesDirectories = allSourceSets.associateBy(PluginSourceSet::platform) {
                when (it.platform) {
                    KotlinPlatform.Apple -> it.appleAssetsDir(project)
                    KotlinPlatform.Js, KotlinPlatform.Jvm -> File(it.resourcesDir, "images")
                    else -> it.resourcesDir
                }
            }
        }

        val bundleTask = project.tasks.register(GENERATE_BUNDLE_TASK_NAME, LibresBundleGenerationTask::class.java) { task ->
            task.group = TASK_GROUP
            task.bundleName = project.appleBundleName
            task.filesToCopy = emptyList()
            task.directoryToCompile = allSourceSets.firstNotNullOfOrNull {
                if (it.platform == KotlinPlatform.Apple) it.appleAssetsDir(project) else null
            }
            task.outputDirectory = allSourceSets.firstNotNullOfOrNull {
                if (it.platform == KotlinPlatform.Apple) File(it.rootDir, "libres-bundles") else null
            }

            task.onlyIf { Os.isFamily(Os.FAMILY_MAC) }
            task.dependsOn(GENERATE_IMAGES_TASK_NAME)
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

        project.tasks.configureEach { projectTask ->
            when {
                projectTask is KotlinNativeCompile -> projectTask.dependsOn(bundleTask, resourcesTask)
                projectTask is KotlinCompile<*> -> projectTask.dependsOn(resourcesTask)
                projectTask is ProcessResources -> projectTask.dependsOn(imagesTask)
                projectTask is Jar -> projectTask.dependsOn(resourcesTask)
                isAndroid && projectTask is GenerateResValues -> projectTask.dependsOn(imagesTask)
                projectTask.name.endsWith("ComposeResourcesForIos") -> projectTask.dependsOn(imagesTask)
            }
        }
    }

    private fun Project.registerSetupIosExportTasks() {
        if (!plugins.hasPlugin(KotlinCocoapodsPlugin::class.java)) return
        val multiplatformExtension = extensions.findByType(KotlinMultiplatformExtension::class.java) ?: return

        multiplatformExtension.cocoapodsExtensionOrNull?.let { cocoapods ->
            cocoapods.ios.deploymentTarget?.let {  deploymentTarget ->
                val exports = getCocoapodsExports(multiplatformExtension)
                (exports + project).setIosDeploymentTarget(deploymentTarget)
            }
        }

        val createBundlesSymlinksTask = tasks.register(CREATE_SYMLINKS_TASK_NAME) { task ->
            task.group = TASK_GROUP
            task.onlyIf { Os.isFamily(Os.FAMILY_MAC) }
            task.doLast {
                val exports = getCocoapodsExports(multiplatformExtension).filter { it.isPluginApplied }
                createBundlesSymLinks(project, exports)
            }
        }

        val setupPodspecTask = tasks.register(SETUP_PODSPEC_TASK_NAME) { task ->
            task.group = TASK_GROUP
            task.doLast {
                multiplatformExtension.cocoapodsExtensionOrNull?.let { cocoapods ->
                    val currentResources = cocoapods.extraSpecAttributes["resources"] ?: ""
                    cocoapods.extraSpecAttributes["resources"] = project.appendAppleBundles(currentResources)
                }
            }
        }

        tasks.configureEach { projectTask ->
            when (projectTask) {
                is PodspecTask -> projectTask.dependsOn(setupPodspecTask)
                is KotlinNativeLink -> projectTask.dependsOn(createBundlesSymlinksTask)
            }
        }
    }

    private fun List<Project>.setIosDeploymentTarget(value: String)  = forEach { project ->
        project.tasks.configureEach { task ->
            if (task is LibresBundleGenerationTask) {
                task.minimumDeploymentTarget = value
            }
        }
    }

    private fun Project.getCocoapodsExports(multiplatformExtension: KotlinMultiplatformExtension): List<Project> {
        val configName = multiplatformExtension.cocoapodsExportConfigurationName ?: return emptyList()
        return configurations.getByName(configName).dependencies.filterIsInstance<ProjectDependency>().map { it.dependencyProject }
    }

    companion object {

        private const val LIBRARY_PACKAGE_NAME = "io.github.skeptick.libres"
        internal const val STRINGS_PACKAGE_NAME = "$LIBRARY_PACKAGE_NAME.strings"
        internal const val IMAGES_PACKAGE_NAME = "$LIBRARY_PACKAGE_NAME.images"

        private const val TASK_GROUP = "libres"
        const val GENERATE_RESOURCES_TASK_NAME = "libresGenerateResources"
        const val GENERATE_STRINGS_TASK_NAME = "libresGenerateStrings"
        const val GENERATE_IMAGES_TASK_NAME = "libresGenerateImages"
        const val GENERATE_BUNDLE_TASK_NAME = "libresGenerateIosBundle"
        const val CREATE_SYMLINKS_TASK_NAME = "libresCreateSymlinkedBundles"
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

        private fun PluginSourceSet.appleAssetsDir(project: Project): File =
            File(resourcesDir, "images/${project.appleBundleName}.xcassets")

        private val Project.isPluginApplied: Boolean
            get() = plugins.hasPlugin(ResourcesPlugin::class.java)

    }

}
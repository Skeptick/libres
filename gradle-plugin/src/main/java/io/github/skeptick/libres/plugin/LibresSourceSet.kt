package io.github.skeptick.libres.plugin

import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

internal class LibresSourceSet(
    val inputDirectory: Directory,
    val outputDirectory: Provider<Directory>,
    val registerGeneratedSources: (Provider<Directory>) -> Unit
)

internal fun Project.LibresSourceSet(
    name: String,
    outputDirectoryName: String,
    registerGeneratedSources: (Provider<Directory>) -> Unit
) = LibresSourceSet(
    inputDirectory = layout.projectDirectory.dir("src/$name/libres"),
    outputDirectory = layout.buildDirectory.dir("generated/libres/$outputDirectoryName/src"),
    registerGeneratedSources = registerGeneratedSources
)

internal val KotlinProjectExtension.targets: List<KotlinTarget>
    get() = when (this) {
        is KotlinSingleTargetExtension<*> -> listOf(target)
        is KotlinMultiplatformExtension -> targets.toList()
        else -> error("Unexpected 'kotlin' extension $this")
    }

internal val KotlinCompilation<*>.isMainCompilation: Boolean
    get() = name == "main"
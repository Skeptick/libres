package io.github.skeptick.libres.plugin

import com.android.build.api.dsl.AndroidSourceSet
import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinWithJavaTarget
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.konan.target.Family
import java.io.File
import java.io.Serializable
import java.util.*

enum class KotlinPlatform {
    Common, Jvm, Android, Js, Apple
}

internal class SourceSet(
    val rootDir: File,
    val sourcesDir: File,
    val resourcesDir: File,
    val platform: KotlinPlatform
) : Serializable

internal fun KotlinSourceSet.createKotlinSourceSet(
    outputDirectory: File,
    platform: KotlinPlatform
): SourceSet {
    val rootDir = File(outputDirectory, platform.name.lowercase())
    val sourcesDirectory = File(rootDir, "src")
    val resourcesDirectory = File(rootDir, "resources")
    kotlin.srcDir(sourcesDirectory)
    // don't add to apples source sets, otherwise compose plugin duplicates them
    if (platform != KotlinPlatform.Apple) resources.srcDir(resourcesDirectory)
    return SourceSet(rootDir = rootDir, sourcesDir = sourcesDirectory, resourcesDir = resourcesDirectory, platform)
}

internal fun KotlinSourceSet.createAndroidSourceSet(
    outputDirectory: File,
    androidMainSourceSet: AndroidSourceSet
): SourceSet {
    val rootDir = File(outputDirectory, "android")
    val sourcesDirectory = File(rootDir, "src")
    val resourcesDirectory = File(rootDir, "resources")
    kotlin.srcDir(sourcesDirectory)
    androidMainSourceSet.kotlin.srcDir(sourcesDirectory)
    androidMainSourceSet.res.srcDir(resourcesDirectory)
    return SourceSet(rootDir = rootDir, sourcesDir = sourcesDirectory, resourcesDir = resourcesDirectory, KotlinPlatform.Android)
}

internal fun List<KotlinTarget>.takeKotlinSourceSet(
    outputDirectory: File
): List<SourceSet> = mapNotNull { target ->
    val platform = target.platform ?: return@mapNotNull null
    val compilation = target.compilations.firstOrNull { it.isMainCompilation }
    compilation?.defaultSourceSet?.createKotlinSourceSet(outputDirectory, platform)
}

internal fun BaseExtension.findAndroidSourceSet(
    hasCommon: Boolean,
    outputDirectory: File,
    kotlinExtension: KotlinProjectExtension
): SourceSet {
    val androidMainSourceSet = sourceSets.getByName(org.gradle.api.tasks.SourceSet.MAIN_SOURCE_SET_NAME)
    val kotlinSourceSet = if (!hasCommon) {
        kotlinExtension.sourceSets.getByName(androidMainSourceSet.name)
    } else {
        val androidTarget = kotlinExtension.targets.first { it is KotlinAndroidTarget }
        androidTarget.compilations.firstNotNullOf { compilation ->
            compilation.kotlinSourceSets.first { sourceSet ->
                sourceSet.name.endsWith(androidMainSourceSet.name, ignoreCase = true)
            }
        }
    }
    return kotlinSourceSet.createAndroidSourceSet(outputDirectory, androidMainSourceSet)
}

// Android isn't kotlin (-_-)
internal val KotlinTarget.platform: KotlinPlatform?
    get() = when (this) {
        is KotlinJvmTarget, is KotlinWithJavaTarget<*, *> -> KotlinPlatform.Jvm
        is KotlinJsIrTarget -> KotlinPlatform.Js
        is KotlinNativeTarget -> when (konanTarget.family) {
            Family.IOS, Family.OSX, Family.TVOS -> KotlinPlatform.Apple
            else -> null
        }
        else -> null
    }

internal val KotlinProjectExtension.targets: List<KotlinTarget>
    get() = when (this) {
        is KotlinSingleTargetExtension<*> -> listOf(target)
        is KotlinMultiplatformExtension -> targets.toList()
        else -> error("Unexpected 'kotlin' extension $this")
    }

internal val KotlinCompilation<*>.isMainCompilation: Boolean
    get() = name == "main"
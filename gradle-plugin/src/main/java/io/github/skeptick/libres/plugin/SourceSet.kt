package io.github.skeptick.libres.plugin

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinSingleTargetExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinWithJavaTarget
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.konan.target.Family

internal enum class KotlinPlatform {
    Common, Jvm, Android, Js, Apple
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
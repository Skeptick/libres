@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

val libs = the<LibrariesForLibs>()

kotlin {
    // Android
    android {
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        packaging.resources.excludes.add("META-INF/*.kotlin_module")
    }

    // JVM
    jvm()

    // JS
    js {
        browser()
    }

    // WasmJS
    wasmJs {
        browser()
    }

    // iOS
    iosArm64()
    iosSimulatorArm64()

    // MacOS
    macosArm64()

    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xexpect-actual-classes",
            "-Xexplicit-api=strict"
        )
    }

    jvmToolchain(17)
}
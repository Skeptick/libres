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
    }

    // JVM
    jvm()

    // iOS
    iosArm64()
    iosSimulatorArm64()

    // watchOS
    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()

    // tvOS
    tvosArm64()
    tvosSimulatorArm64()

    // MacOS
    macosArm64()

    // Linux
    linuxX64()
    linuxArm64()

    // Windows
    mingwX64()

    // JavaScript
    js {
        browser()
        nodejs()
    }

    // WASM JS
    wasmJs {
        browser()
        nodejs()
        d8()
    }

    // WASM WASI
    wasmWasi {
        nodejs()
    }

    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xexpect-actual-classes",
            "-Xexplicit-api=strict"
        )
    }

    jvmToolchain(17)
}
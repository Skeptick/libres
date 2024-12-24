@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xexpect-actual-classes",
            "-Xexplicit-api=strict"
        )
    }

    jvm()
    androidTarget {
        publishAllLibraryVariants()
    }
    js {
        browser()
    }
    wasmJs {
        browser()
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosArm64()
    macosX64()

    jvmToolchain(17)
}
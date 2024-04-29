@file:OptIn(org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl::class)

plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

kotlin {
    jvm()
    androidTarget {
        publishAllLibraryVariants()
    }
    js(IR) {
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

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
            freeCompilerArgs += "-Xexpect-actual-classes"
        }
    }
}
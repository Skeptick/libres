@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("com.vanniktech.maven.publish")
    id("io.github.skeptick.libres.plurals")
}

kotlin {
    // Android
    android {
        namespace = "io.github.skeptick.libres.plurals"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        packaging.resources.excludes.add("META-INF/*.kotlin_module")
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

    // Linux
    linuxX64()
    linuxArm64()

    // Windows
    mingwX64()

    // MacOS
    macosArm64()

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

    metadata {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    freeCompilerArgs.add("-Xexplicit-api=strict")
                }
            }
        }
    }

    sourceSets {
        commonTest {
            dependencies {
                implementation(libs.test.kotlin.core)
            }
        }
    }

    jvmToolchain(17)
}

pluralRulesGenerator {
    // https://raw.githubusercontent.com/unicode-org/cldr-json/refs/heads/main/cldr-json/cldr-core/supplemental/plurals.json
    rulesFile = layout.projectDirectory.file("plurals.json")
    packageName = "io.github.skeptick.libres.plurals"
    sourceDirectory = layout.projectDirectory.dir("src/commonMain/kotlin/")
    testsDirectory = layout.projectDirectory.dir("src/commonTest/kotlin/")
}
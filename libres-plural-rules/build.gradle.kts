@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

plugins {
    kotlin("multiplatform")
    id("android-setup-plugin")
    id("com.vanniktech.maven.publish")
    id("io.github.skeptick.libres.plurals")
}

kotlin {
    metadata {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    freeCompilerArgs.add("-Xexplicit-api=strict")
                }
            }
        }
    }

    // Android
    androidTarget {
        publishLibraryVariants("release")
    }

    // JVM
    jvm()

    // iOS
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    // watchOS
    watchosX64()
    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()

    // tvOS
    tvosX64()
    tvosArm64()
    tvosSimulatorArm64()

    // Linux
    linuxX64()
    linuxArm64()

    // Windows
    mingwX64()

    // MacOS
    macosX64()
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

    sourceSets {
        commonTest {
            dependencies {
                implementation(libs.test.kotlin.core)
            }
        }
    }

    jvmToolchain(17)
}

android {
    namespace = "io.github.skeptick.libres.plurals"
}

pluralRulesGenerator {
    // https://raw.githubusercontent.com/unicode-org/cldr-json/refs/heads/main/cldr-json/cldr-core/supplemental/plurals.json
    rulesFile = layout.projectDirectory.file("plurals.json")
    packageName = "io.github.skeptick.libres.plurals"
    sourceDirectory = layout.projectDirectory.dir("src/commonMain/kotlin/")
    testsDirectory = layout.projectDirectory.dir("src/commonTest/kotlin/")
}
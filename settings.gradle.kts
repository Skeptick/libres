@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("plural-rules-generator")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":libres-core", ":libres-plural-rules", ":gradle-plugin")
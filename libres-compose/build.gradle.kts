plugins {
    id("multiplatform-setup")
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    applyDefaultHierarchyTemplate()

    android {
        namespace = "io.github.skeptick.libres.compose"
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.libresCore)
                implementation(libs.coroutines.core)
                implementation(libs.compose.ui)
            }
        }
    }
}

compose.resources {
    generateResClass = never
}
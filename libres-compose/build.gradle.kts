plugins {
    id("multiplatform-setup")
    id("android-setup-plugin")
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.compose")
}

android {
    namespace = "io.github.skeptick.libres.compose"
}

kotlin {
    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain {
            dependencies {
                api(projects.libresCore)
                implementation(libs.coroutines.core)
                implementation(compose.ui)
            }
        }
    }
}
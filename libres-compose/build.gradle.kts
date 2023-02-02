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
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.libresCore)
                compileOnly(compose.ui)
            }
        }
    }
}
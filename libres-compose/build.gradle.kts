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
        commonMain {
            dependencies {
                api(projects.libresCore)
                compileOnly(compose.ui)
            }
        }
    }
}
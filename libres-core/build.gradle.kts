plugins {
    id("multiplatform-setup")
    id("android-setup-plugin")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "io.github.skeptick.libres"
}

kotlin {
    tvosX64()
    tvosSimulatorArm64()
    tvosArm64()

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.libresPluralRules)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.core)
            }
        }

        wasmJsMain {
            dependencies {
                implementation(libs.browser)
            }
        }
    }
}
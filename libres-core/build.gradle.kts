plugins {
    id("multiplatform-setup")
    id("com.vanniktech.maven.publish")
}

kotlin {
    applyDefaultHierarchyTemplate()

    android {
        namespace = "io.github.skeptick.libres"
    }

    tvosSimulatorArm64()
    tvosArm64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.libresPluralRules)
            }
        }

        wasmJsMain {
            dependencies {
                implementation(libs.browser)
            }
        }
    }
}
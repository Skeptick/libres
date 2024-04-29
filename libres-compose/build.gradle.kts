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
                implementation(compose.ui)
            }
        }

        val wasmAndJsCommon by creating {
            dependsOn(commonMain.get())
        }

        jsMain {
            dependsOn(wasmAndJsCommon)
            dependencies {
                compileOnly(libs.kotlinx.coroutines.core)
            }
        }

        wasmJsMain {
            dependsOn(wasmAndJsCommon)
            dependencies {
                compileOnly(libs.kotlinx.coroutines.core)
            }
        }
    }
}
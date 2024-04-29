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
        androidMain {
            dependencies {
                implementation(libs.androidx.core)
                compileOnly(libs.robovm)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.icu4j)
            }
        }

        val wasmAndJsCommon by creating {
            dependsOn(commonMain.get())
            jsMain.get().dependsOn(this)
            wasmJsMain.get().dependsOn(this)
        }

        val appleAndJsMain by creating {
            dependsOn(commonMain.get())
            appleMain.get().dependsOn(this)
            wasmAndJsCommon.dependsOn(this)
        }

        val uikitMain by creating {
            dependsOn(appleMain.get())
            iosMain.get().dependsOn(this)
            tvosMain.get().dependsOn(this)
        }
    }
}
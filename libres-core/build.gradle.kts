plugins {
    id("multiplatform-setup")
    id("android-setup-plugin")
    id("com.vanniktech.maven.publish")
}

android {
    namespace = "io.github.skeptick.libres"
}

kotlin {
    sourceSets {
        val commonMain by getting
        val appleMain by getting
        val jsMain by getting

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

        val appleAndJsMain by creating {
            dependsOn(commonMain)
            appleMain.dependsOn(this)
            jsMain.dependsOn(this)
        }
    }
}
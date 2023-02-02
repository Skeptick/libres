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
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.core)
                compileOnly(libs.robovm)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.icu4j)
            }
        }
    }
}
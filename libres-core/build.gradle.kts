plugins {
    id("multiplatform-setup")
    id("com.vanniktech.maven.publish")
}

kotlin {
    applyDefaultHierarchyTemplate()

    android {
        namespace = "io.github.skeptick.libres"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.libresPluralRules)
            }
        }
    }
}
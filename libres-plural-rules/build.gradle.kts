plugins {
    id("multiplatform-setup")
    id("com.vanniktech.maven.publish")
    id("io.github.skeptick.libres.plurals")
}

kotlin {
    applyDefaultHierarchyTemplate()

    android {
        namespace = "io.github.skeptick.libres.plurals"
    }

    sourceSets {
        commonTest {
            dependencies {
                implementation(libs.test.kotlin.core)
            }
        }
    }

    jvmToolchain(17)
}

pluralRulesGenerator {
    // https://raw.githubusercontent.com/unicode-org/cldr-json/refs/heads/main/cldr-json/cldr-core/supplemental/plurals.json
    rulesFile = layout.projectDirectory.file("plurals.json")
    packageName = "io.github.skeptick.libres.plurals"
    sourceDirectory = layout.projectDirectory.dir("src/commonMain/kotlin/")
    testsDirectory = layout.projectDirectory.dir("src/commonTest/kotlin/")
}
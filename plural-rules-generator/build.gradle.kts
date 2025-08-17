plugins {
    `java-gradle-plugin`
    kotlin("jvm") version embeddedKotlinVersion
    kotlin("plugin.serialization") version embeddedKotlinVersion
}

version = "1.0.0"

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(gradleApi())
    implementation(libs.kotlinpoet)
    implementation(libs.serialization.json)
}

gradlePlugin {
    plugins {
        create("plural-rules-generator-plugin") {
            id = "io.github.skeptick.libres.plurals"
            implementationClass = "io.github.skeptick.libres.plurals.plugin.PluralRulesGeneratorPlugin"
        }
    }
}
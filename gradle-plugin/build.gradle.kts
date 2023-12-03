import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
    kotlin("jvm")
    id("java-gradle-plugin")
    id("com.vanniktech.maven.publish")
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    implementation(projects.libresCore)
    implementation(gradleApi())
    implementation(libs.jackson.xml)
    implementation(libs.jackson.kotlin)
    implementation(libs.kotlinpoet)
    implementation(libs.javacv)
    compileOnly(libs.android.sdk.tools)
    compileOnly(libs.plugin.kotlin)
    compileOnly(libs.plugin.android)
    testImplementation(libs.junit)
}

gradlePlugin {
    plugins {
        create("libres-plugin") {
            id = "io.github.skeptick.libres"
            implementationClass = "io.github.skeptick.libres.plugin.ResourcesPlugin"
        }
    }
}

sourceSets {
    getByName("main").java.srcDir("generated")
}

tasks.register("libresVersion") {
    val outputDir = file("generated")
    inputs.property("version", version)
    outputs.dir(outputDir)

    doLast {
        val text = """
            // Generated file. Do not edit!
            package io.github.skeptick.libres
            
            val VERSION = "${project.version}"
        """.trimIndent()

        val versionFile = file("$outputDir/io/github/skeptick/libres/Version.kt")
        versionFile.parentFile.mkdirs()
        versionFile.writeText(text)
    }
}

afterEvaluate {
    tasks.withType(Jar::class.java).configureEach { dependsOn("libresVersion") }
    tasks.withType(KotlinCompile::class.java).configureEach { dependsOn("libresVersion") }
}
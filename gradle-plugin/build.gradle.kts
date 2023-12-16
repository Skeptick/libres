plugins {
    kotlin("jvm")
    id("java-gradle-plugin")
    id("com.vanniktech.maven.publish")
}

version = property("VERSION_NAME").toString()

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

val libresVersion = tasks.register("libresVersion") {
    val outputDir = file("generated")
    inputs.property("version", version)
    outputs.dir(outputDir)

    doFirst {
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

sourceSets.main.configure {
    kotlin.srcDir(libresVersion)
}
plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.plugin.android)
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.serialization)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
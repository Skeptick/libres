plugins {
    id("com.android.library")
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 16
        targetSdk = 33
    }

    buildFeatures {
        buildConfig = false
    }

    compileOptions.apply {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
}
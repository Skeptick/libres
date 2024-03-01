plugins {
    id("com.android.library")
}

android {
    compileSdk = 34

    defaultConfig {
        minSdk = 16
    }

    buildFeatures {
        buildConfig = false
    }
}
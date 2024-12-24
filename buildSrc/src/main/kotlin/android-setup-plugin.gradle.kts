plugins {
    id("com.android.library")
}

android {
    compileSdk = 35

    defaultConfig {
        minSdk = 16
    }

    buildFeatures {
        buildConfig = false
    }
}
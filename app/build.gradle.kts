
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.nothing.musicvisualizer"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.nothing.musicvisualizer"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}


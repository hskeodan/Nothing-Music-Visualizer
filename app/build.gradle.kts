
plugins {
    id("com.android.application") version "8.13.2"
    id("org.jetbrains.kotlin.android") version "2.2.20"
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

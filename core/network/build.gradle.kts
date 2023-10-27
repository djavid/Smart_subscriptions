@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.djavid.core.network"

    defaultConfig {
        minSdk = 21
        targetSdk = 34
        compileSdk = 33
    }

}

dependencies {
    implementation(project(":core:ui"))

//    implementation(libs.firebase.crashlytics)
}
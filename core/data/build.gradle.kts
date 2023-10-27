@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.djavid.core.data"

    defaultConfig {
        minSdk = 21
        targetSdk = 34
        compileSdk = 33
    }

}

dependencies {
    implementation(project(":core:ui"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)
    implementation(libs.coroutines)

    implementation(libs.kodein)
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.junit)
    implementation(libs.joda.time)
}
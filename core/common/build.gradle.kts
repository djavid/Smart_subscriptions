@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.djavid.core.common"

    defaultConfig {
        minSdk = 21
        targetSdk = 34
        compileSdk = 33
    }

}

dependencies {
    implementation(project(":core:data"))

    implementation(libs.kodein)
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.junit)
}
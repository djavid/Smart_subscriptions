@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.djavid.features.sort"
    compileSdk = 33
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:utils"))
    implementation(project(":core:data"))
    implementation(project(":core:analytics"))

    implementation(libs.kodein)
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.junit)
}
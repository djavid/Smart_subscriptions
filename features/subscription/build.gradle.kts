@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

apply {
    from("${rootDir}/gradle/common_config.gradle")
}

android {
    namespace = "com.djavid.features.subscription"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:analytics"))

    implementation(libs.coroutines)
}
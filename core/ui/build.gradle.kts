@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

apply {
    from("${rootDir}/gradle/common_config.gradle")
}

android {
    namespace = "com.djavid.core.ui"
}

dependencies {
    implementation(project(":core:common"))

    api(libs.material)
    api(libs.androidx.activity)
    api(libs.glide)
    api(libs.appcompat)
    api(libs.recyclerview)
    api(libs.core.ktx)
    api(libs.fragment.ktx)
}
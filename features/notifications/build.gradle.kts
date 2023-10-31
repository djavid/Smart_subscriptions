@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

apply {
    from("${rootDir}/gradle/common_config.gradle")
}

android {
    namespace = "com.djavid.features.notifications"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:utils"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:analytics"))
    implementation(project(":features:notification"))

    implementation(libs.kodein)
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.junit)
    implementation(libs.joda.time)
}
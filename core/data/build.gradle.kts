@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.sqldelight)
}

apply {
    from("${rootDir}/gradle/common_config.gradle")
}

android {
    namespace = "com.djavid.core.data"
}

sqldelight {
    database("Database") {
        packageName = "com.djavid.smartsubs.db"
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:analytics"))

    api(libs.joda.time)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.coroutines)
    implementation(libs.play.core)
    implementation(libs.sqldelight.android.driver)
}
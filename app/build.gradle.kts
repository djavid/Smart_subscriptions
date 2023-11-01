@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
}

apply {
    from("${rootDir}/gradle/common_config.gradle")
}

android {
    namespace = "com.djavid.smartsubs"
    buildToolsVersion = "34.0.0"

    defaultConfig {
        applicationId = "com.djavid.smartsubs"
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        compileSdk = libs.versions.compileSdk.get().toInt()
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            versionNameSuffix = "-release"
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            resValue("bool", "FIREBASE_ANALYTICS_DEACTIVATED", "false")
            buildConfigField("boolean", "ANALYTICS_DEACTIVATED", "false")
        }
        debug {
            isDebuggable = true
            versionNameSuffix = "-debug"
            resValue("bool", "FIREBASE_ANALYTICS_DEACTIVATED", "true")
            buildConfigField("boolean", "ANALYTICS_DEACTIVATED", "true")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    lint {
        abortOnError = false
    }
}

//sqldelight {
//    databases {
//        create("Database") {
//            packageName.set("com.djavid.smartsubs.db")
//        }
//    }
//}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(project(":core:analytics"))

    implementation(project(":features:notification"))
    implementation(project(":features:notifications"))
    implementation(project(":features:sort"))
    implementation(project(":features:home"))
    implementation(project(":features:create"))
    implementation(project(":features:subscribe_media"))
    implementation(project(":features:sub_list"))
    implementation(project(":features:currency_list"))
    implementation(project(":features:subscription"))

    //kotlin
    implementation(libs.kotlin.stdlib)

    //tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.espresso.core)

    //android
    implementation(libs.recyclerview)
    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    implementation(libs.constraintlayout)
    implementation(libs.cardview)
    implementation(libs.coordinatorlayout)
    implementation(libs.material)
    implementation(libs.localbroadcastmanager)

    //core
    implementation(libs.orbit.viewmodel)
    implementation(libs.orbit.compose)
    implementation(libs.kodein)
    implementation(libs.retrofit)
//    implementation(libs.sqldelight.android.driver)

    //coroutines
    implementation(libs.coroutines)

    //lifecycle
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)

    //time
    implementation(libs.prettytime)
    implementation(libs.joda.time)

    //firebase
    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.auth)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)

    //images
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)
    implementation(libs.circleimageview)

    //google
    implementation(libs.play.core)
    implementation(libs.play.core.ktx)
    implementation(libs.gson)

    //work manager
    implementation(libs.work.runtime.ktx)

    //analytics
    implementation(libs.facebook.android.sdk)
    implementation(libs.appmetrica)
}
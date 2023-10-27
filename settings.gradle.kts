rootProject.name = "Умные подписки"

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
    }
}

//MODULES
include(":app")

// core
include(":core:ui")
include(":core:common")
include(":core:utils")
include(":core:data")
include(":core:analytics")
include(":core:network")

// features
include(":features:home")
include(":features:subscription")
include(":features:create")
include(":features:sort")
include(":features:notification")
include(":features:notifications")
include(":features:currency_list")
include(":features:sub_list")
include(":features:subscribe_media")

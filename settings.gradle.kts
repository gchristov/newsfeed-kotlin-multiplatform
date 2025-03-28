enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("gradle-plugins")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("com.gradle.enterprise") version("3.16.2")
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(uri("https://jitpack.io")) // For compose-html dependency
    }
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlways()
    }
}

rootProject.name = "newsfeed-kotlin-multiplatform"

// Multiplatform
include(":multiplatform:auth:data")
include(":multiplatform:common:firebase")
include(":multiplatform:common:kotlin")
include(":multiplatform:common:mvvm")
include(":multiplatform:common:mvvm-test")
include(":multiplatform:common:network")
include(":multiplatform:common:persistence")
include(":multiplatform:common:test")
include(":multiplatform:feed:data")
include(":multiplatform:feed:feature")
include(":multiplatform:feed:test-fixtures")
include(":multiplatform:post:data")
include(":multiplatform:post:feature")
include(":multiplatform:post:test-fixtures")
include(":multiplatform:umbrella")
// Android
include(":android:app")
include(":android:common:compose")
include(":android:common:compose-test")
include(":android:common:design")
include(":android:common:firebase")
include(":android:common:navigation")
include(":android:common:test")
include(":android:feed:feature")
include(":android:feed:test-fixtures")
include(":android:post:feature")
include(":android:post:test-fixtures")

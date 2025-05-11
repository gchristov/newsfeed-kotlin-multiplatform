enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("gradle-plugins")
    repositories {
        google()
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.develocity") version("3.18.1")
}

dependencyResolutionManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven(uri("https://jitpack.io")) // For compose-html dependency
    }
}

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
        termsOfUseAgree.set("yes")
        publishing.onlyIf { true }
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

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "newsfeed-kotlin-multiplatform"

apply {
    from("modules.gradle.kts")
}

includeBuild("gradle-plugins")
include(":multiplatform:post:data")
include(":multiplatform:post:feature")
include(":multiplatform:post:test-fixtures")
include(":android:post:feature")
include(":android:post:test-fixtures")

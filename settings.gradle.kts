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
// Multiplatform
include(":multiplatform:common:di")
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
// Android
include(":android:app")
include(":android:common:compose")
include(":android:common:compose-test")
include(":android:common:design")
include(":android:common:navigation")
include(":android:common:test")
include(":android:feed:feature")
include(":android:feed:test-fixtures")
include(":android:post:feature")
include(":android:post:test-fixtures")

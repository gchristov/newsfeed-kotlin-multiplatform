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

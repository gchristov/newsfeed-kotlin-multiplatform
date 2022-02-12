plugins {
    id("kmm-module-plugin")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.kmmFeedData)
            }
        }
    }
}
plugins {
    id("mpl-module-plugin")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.multiplatform.feed.data)
            }
        }
    }
}
plugins {
    id("mpl-module-plugin")
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.multiplatform.feed.testfixtures"
    }
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
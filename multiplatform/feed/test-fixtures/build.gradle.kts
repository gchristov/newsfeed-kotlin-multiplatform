plugins {
    alias(libs.plugins.newsfeed.mpl.module)
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
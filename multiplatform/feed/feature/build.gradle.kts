plugins {
    alias(libs.plugins.newsfeed.mpl.feature)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.multiplatform.feed.feature"
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.multiplatform.feed.data)
                api(projects.multiplatform.feed.testFixtures)
                api(projects.multiplatform.post.testFixtures) // Needed for fake post repository
            }
        }
    }
}

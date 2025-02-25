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
        commonMain.dependencies {
            api(projects.multiplatform.common.firebase)
            api(projects.multiplatform.feed.data)
            api(projects.multiplatform.feed.testFixtures)
            api(projects.multiplatform.post.testFixtures) // Needed for fake post repository
        }
    }
}

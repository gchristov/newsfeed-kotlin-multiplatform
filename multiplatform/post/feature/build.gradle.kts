plugins {
    alias(libs.plugins.newsfeed.mpl.feature)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.multiplatform.post.feature"
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.multiplatform.post.data)
            api(projects.multiplatform.post.testFixtures)
        }
    }
}

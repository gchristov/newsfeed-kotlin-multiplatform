plugins {
    alias(libs.plugins.newsfeed.mpl.module)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.multiplatform.umbrella.di"
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.multiplatform.feed.feature)
            implementation(projects.multiplatform.feed.data)
            implementation(projects.multiplatform.post.feature)
            implementation(projects.multiplatform.post.data)
        }
    }
}
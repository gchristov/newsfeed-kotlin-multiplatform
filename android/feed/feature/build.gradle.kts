plugins {
    alias(libs.plugins.newsfeed.android.feature)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.android.feed.feature"
    }
}

dependencies {
    implementation(projects.multiplatform.feed.feature)
    androidTestImplementation(projects.android.feed.testFixtures)
}
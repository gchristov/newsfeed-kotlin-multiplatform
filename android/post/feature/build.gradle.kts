plugins {
    alias(libs.plugins.newsfeed.android.feature)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.android.post.feature"
    }
}

dependencies {
    implementation(projects.multiplatform.post.feature)
    androidTestImplementation(projects.android.post.testFixtures)
}
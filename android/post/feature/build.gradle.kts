plugins {
    id("android-feature-plugin")
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
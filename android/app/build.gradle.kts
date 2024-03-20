plugins {
    alias(libs.plugins.newsfeed.android.applicationBinary)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed"
        applicationId = "com.gchristov.newsfeed"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    // Multiplatform leaf modules
    implementation(projects.multiplatform.feed.feature)
    implementation(projects.multiplatform.post.feature)
    // Android leaf modules
    implementation(projects.android.common.design)
    implementation(projects.android.feed.feature)
    implementation(projects.android.post.feature)
}
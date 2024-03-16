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
    implementation(projects.multiplatform.common.di)
    implementation(projects.android.common.design)
    // Feature modules
    implementation(projects.android.feed.feature)
    implementation(projects.android.post.feature)
}
plugins {
    id("android-application-binary-plugin")
}

android {
    defaultConfig {
        applicationId = "com.gchristov.newsfeed"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(projects.kmmCommonDi)
    implementation(projects.commonDesign)
    // Feature modules
    implementation(projects.android.feed.feature)
    implementation(projects.android.post.feature)
}
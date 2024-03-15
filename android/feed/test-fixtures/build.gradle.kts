plugins {
    id("android-module-plugin")
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.android.feed.testfixtures"
    }
}

dependencies {
    implementation(projects.android.common.composeTest)
    implementation(projects.android.feed.feature)
}

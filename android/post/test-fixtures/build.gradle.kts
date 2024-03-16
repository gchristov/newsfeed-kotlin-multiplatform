plugins {
    alias(libs.plugins.newsfeed.android.module)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.android.post.testfixtures"
    }
}

dependencies {
    implementation(projects.android.common.composeTest)
    implementation(projects.android.post.feature)
}

plugins {
    id("android-module-plugin")
}

dependencies {
    implementation(projects.commonComposeTest)
    implementation(projects.android.feed.feature)
}

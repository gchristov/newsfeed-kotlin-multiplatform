plugins {
    id("android-module-plugin")
}

dependencies {
    implementation(projects.android.common.composeTest)
    implementation(projects.android.post.feature)
}

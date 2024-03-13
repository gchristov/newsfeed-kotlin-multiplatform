plugins {
    id("android-feature-plugin")
}

dependencies {
    implementation(projects.multiplatform.post.feature)
    androidTestImplementation(projects.android.post.testFixtures)
}
plugins {
    id("android-feature-plugin")
}

dependencies {
    implementation(projects.kmmPost)
    androidTestImplementation(projects.postTestFixtures)
}
plugins {
    id("android-feature-plugin")
}

android {
    defaultConfig {
        buildConfigField("int", "PAGE_SIZE", "10")
    }
}

dependencies {
    implementation(projects.kmmFeed)
    implementation(projects.android.post.feature)
    androidTestImplementation(projects.feedTestFixtures)
}
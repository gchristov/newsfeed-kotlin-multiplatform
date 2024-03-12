plugins {
    id("android-feature-plugin")
}

android {
    defaultConfig {
        buildConfigField("int", "PAGE_SIZE", "10")
    }
}

dependencies {
    implementation(projects.multiplatform.feed.feature)
    androidTestImplementation(projects.android.feed.testFixtures)
}
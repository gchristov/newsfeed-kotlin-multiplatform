plugins {
    id("common-feature-plugin")
}

android {
    defaultConfig {
        buildConfigField("int", "PAGE_SIZE", "10")
    }
}

dependencies {
    implementation(projects.kmmFeed)
    implementation(projects.post)
    androidTestImplementation(projects.feedTestFixtures)
}
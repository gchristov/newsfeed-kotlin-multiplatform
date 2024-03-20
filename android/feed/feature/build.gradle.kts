plugins {
    alias(libs.plugins.newsfeed.android.feature)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.android.feed.feature"
        buildConfigField("int", "PAGE_SIZE", "10")
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.multiplatform.feed.feature)
    androidTestImplementation(projects.android.feed.testFixtures)
}
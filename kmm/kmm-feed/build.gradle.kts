plugins {
    id("mpl-feature-plugin")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.kmmFeedData)
                api(projects.kmmFeedTestFixtures)
                api(projects.multiplatform.post.testFixtures) // Needed for fake post repository
            }
        }
    }
}

plugins {
    id("mpl-feature-plugin")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.multiplatform.feed.data)
                api(projects.multiplatform.feed.testFixtures)
                api(projects.multiplatform.post.testFixtures) // Needed for fake post repository
            }
        }
    }
}

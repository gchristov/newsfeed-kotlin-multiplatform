plugins {
    id("kmm-common-feature-plugin")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.kmmFeedData)
                api(projects.kmmFeedTestFixtures)
            }
        }
    }
}

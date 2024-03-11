plugins {
    id("mpl-feature-plugin")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.kmmPostData)
                api(projects.kmmPostTestFixtures)
            }
        }
    }
}

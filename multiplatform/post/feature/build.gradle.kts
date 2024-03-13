plugins {
    id("mpl-feature-plugin")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.multiplatform.post.data)
                api(projects.multiplatform.post.testFixtures)
            }
        }
    }
}

plugins {
    id("mpl-module-plugin")
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.multiplatform.post.testfixtures"
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.multiplatform.post.data)
            }
        }
    }
}
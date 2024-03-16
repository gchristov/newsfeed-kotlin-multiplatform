val packageId = "com.gchristov.newsfeed.multiplatform.feed.data"

plugins {
    alias(libs.plugins.newsfeed.mpl.data)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.multiplatform.feed.data"
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.multiplatform.post.data) // Needed for knowing if post is favourite
            }
        }
    }
}

sqldelight {
    database("FeedSqlDelightDatabase") {
        packageName = packageId
    }
}

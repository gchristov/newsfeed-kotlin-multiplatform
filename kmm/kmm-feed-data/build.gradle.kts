val packageId = "com.gchristov.newsfeed.kmmfeeddata"

plugins {
    id("mpl-data-plugin")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.kmmPostData) // Needed for knowing if post is favourite
            }
        }
    }
}

sqldelight {
    database("FeedSqlDelightDatabase") {
        packageName = packageId
    }
}

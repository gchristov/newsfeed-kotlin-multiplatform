val packageId = "com.gchristov.newsfeed.multiplatform.post.data"

plugins {
    id("mpl-data-plugin")
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.multiplatform.post.data"
    }
}

sqldelight {
    database("PostSqlDelightDatabase") {
        packageName = packageId
    }
}
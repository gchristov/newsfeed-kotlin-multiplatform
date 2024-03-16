val packageId = "com.gchristov.newsfeed.multiplatform.post.data"

plugins {
    alias(libs.plugins.newsfeed.mpl.data)
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
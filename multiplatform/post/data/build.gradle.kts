val packageId = "com.gchristov.newsfeed.multiplatform.post.data"

plugins {
    id("mpl-data-plugin")
}

sqldelight {
    database("PostSqlDelightDatabase") {
        packageName = packageId
    }
}
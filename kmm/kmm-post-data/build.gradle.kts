val packageId = "com.gchristov.newsfeed.kmmpostdata"

plugins {
    id("mpl-data-plugin")
}

sqldelight {
    database("PostSqlDelightDatabase") {
        packageName = packageId
    }
}
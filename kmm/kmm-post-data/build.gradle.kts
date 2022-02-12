val packageId = "com.gchristov.newsfeed.kmmpostdata"

plugins {
    id("kmm-data-plugin")
}

sqldelight {
    database("PostSqlDelightDatabase") {
        packageName = packageId
    }
}
val packageId = "com.gchristov.newsfeed.kmmfeeddata"

plugins {
    id("kmm-common-data-plugin")
}

sqldelight {
    database("FeedSqlDelightDatabase") {
        packageName = packageId
    }
}

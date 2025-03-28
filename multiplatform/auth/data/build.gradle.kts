val packageId = "com.gchristov.newsfeed.multiplatform.auth.data"

plugins {
    alias(libs.plugins.newsfeed.mpl.data)
}

android {
    defaultConfig {
        namespace = packageId
    }
}

sqldelight {
    databases {
        create("AuthSqlDelightDatabase") {
            packageName.set(packageId)
        }
    }
}
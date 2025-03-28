val packageId = "com.gchristov.newsfeed.multiplatform.feed.data"

plugins {
    alias(libs.plugins.newsfeed.mpl.data)
}

android {
    defaultConfig {
        namespace = packageId
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.multiplatform.post.data)
            implementation(projects.multiplatform.auth.data)
            implementation(projects.multiplatform.common.firebase)
        }
    }
}

sqldelight {
    databases {
        create("FeedSqlDelightDatabase") {
            packageName.set(packageId)
        }
    }
}

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
            api(projects.multiplatform.post.data) // Needed for knowing if post is favourite
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

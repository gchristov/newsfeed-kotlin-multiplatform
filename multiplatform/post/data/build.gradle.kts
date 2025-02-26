val packageId = "com.gchristov.newsfeed.multiplatform.post.data"

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
        create("PostSqlDelightDatabase") {
            packageName.set(packageId)
        }
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.multiplatform.common.firebase)
        }
    }
}
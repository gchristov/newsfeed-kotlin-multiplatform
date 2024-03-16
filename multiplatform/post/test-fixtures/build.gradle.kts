plugins {
    alias(libs.plugins.newsfeed.mpl.module)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.multiplatform.post.testfixtures"
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.multiplatform.post.data)
        }
    }
}
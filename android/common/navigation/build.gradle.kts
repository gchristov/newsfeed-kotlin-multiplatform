plugins {
    alias(libs.plugins.newsfeed.android.module)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.android.common.navigation"
    }
}

dependencies {
    implementation(projects.multiplatform.common.kotlin)
}

plugins {
    id("android-module-plugin")
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.android.common.navigation"
    }
}

dependencies {
    implementation(projects.multiplatform.common.di)
}

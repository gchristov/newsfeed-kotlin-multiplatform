plugins {
    alias(libs.plugins.newsfeed.android.module)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.android.common.firebase"
    }
}

dependencies {
    api(platform(libs.google.firebase))
    implementation(libs.google.crashlytics)
}

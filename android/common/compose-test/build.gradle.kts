plugins {
    alias(libs.plugins.newsfeed.android.module)
    alias(libs.plugins.newsfeed.android.compose)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.android.common.composetest"
    }
}

/*
This module is used in other test modules. Common dependencies are linked to the 'main'
source sets, and marked as `api`, rather than 'test'. This is because 'test' source-specific
dependencies and code are local to the relevant module and cannot be accesses by other modules.
 */
dependencies {
    implementation(projects.android.common.compose)
    api(libs.androidx.compose.ui.test.junit)
    debugApi(libs.androidx.compose.ui.test.manifest)
}

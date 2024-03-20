plugins {
    alias(libs.plugins.newsfeed.android.module)
    alias(libs.plugins.newsfeed.android.compose)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.android.common.compose"
    }
}

dependencies {
    api(libs.androidx.compose.liveData)
    api(libs.androidx.compose.compiler)
    api(libs.androidx.activityCompose)
    api(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.icons)
    implementation(libs.androidx.compose.material)
    implementation(libs.coil)
    implementation(libs.composeHtml)
    implementation(projects.android.common.design)
}

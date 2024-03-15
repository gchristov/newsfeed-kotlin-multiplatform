import com.gchristov.newsfeed.gradleplugins.Deps

plugins {
    id("android-module-plugin")
    id("android-compose-plugin")
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.android.common.compose"
    }
}

dependencies {
    api(Deps.Android.Compose.liveData)
    api(Deps.Android.Compose.compiler)
    api(Deps.Android.Compose.activity)
    api(Deps.Android.Compose.uiTooling)
    api(Deps.Android.Compose.foundation)
    api(Deps.Android.Compose.foundationLayout)
    api(Deps.Android.Compose.materialIcons)
    implementation(Deps.Android.Compose.material)
    implementation(Deps.Android.Compose.coil)
    implementation(Deps.Android.Compose.html)
    implementation(libs.accompanist.swipeRefresh)
    implementation(projects.android.common.design)
}

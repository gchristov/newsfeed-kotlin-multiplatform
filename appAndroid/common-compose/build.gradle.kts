import com.gchristov.newsfeed.gradleplugins.Deps

plugins {
    id("android-module-plugin")
    id("android-compose-plugin")
}

dependencies {
    api(Deps.Compose.liveData)
    api(Deps.Compose.compiler)
    api(Deps.Compose.activity)
    api(Deps.Compose.uiTooling)
    api(Deps.Compose.foundation)
    api(Deps.Compose.foundationLayout)
    api(Deps.Compose.materialIcons)
    implementation(Deps.Compose.material)
    implementation(Deps.Compose.coil)
    implementation(Deps.Compose.html)
    implementation(Deps.Accompanist.swipeRefresh)
    implementation(projects.commonDesign)
}

import com.gchristov.newsfeed.gradleplugins.Deps

plugins {
    id("android-module-plugin")
    id("android-compose-plugin")
}

/*
This module is used in other test modules. Common dependencies are linked to the 'main'
source sets, and marked as `api`, rather than 'test'. This is because 'test' source-specific
dependencies and code are local to the relevant module and cannot be accesses by other modules.
 */
dependencies {
    implementation(projects.commonCompose)
    api(Deps.Compose.uiTestJunit)
    debugApi(Deps.Compose.uiTestManifest)
}

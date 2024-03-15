plugins {
    id("android-base-plugin") // Avoids circular dependencies in android-module-plugin
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.android.common.test"
    }
}

/*
This module is used in other test modules. Common dependencies are linked to the 'main'
source sets, and marked as `api`, rather than 'test'. This is because 'test' source-specific
dependencies and code are local to the relevant module and cannot be accesses by other modules.
 */
dependencies {
    api(libs.test.junit)
}

import com.gchristov.newsfeed.gradleplugins.Deps

plugins {
    id("mpl-module-plugin")
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.multiplatform.common.mvvmtest"
    }
}

kotlin {
    /*
    This module is used in other test modules. Common dependencies are linked to the 'main'
    source sets, and marked as `api`, rather than 'test'. This is because 'test' source-specific
    dependencies and code are local to the relevant module and cannot be accesses by other modules.
     */
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(Deps.Multiplatform.Mvvm.test)
            }
        }
    }
}

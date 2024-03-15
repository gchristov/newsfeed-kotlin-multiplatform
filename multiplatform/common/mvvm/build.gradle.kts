import com.gchristov.newsfeed.gradleplugins.Deps

plugins {
    id("mpl-module-plugin")
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.multiplatform.common.mvvm"
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(Deps.Multiplatform.Mvvm.liveData)
            }
        }
    }
}

import com.gchristov.newsfeed.gradleplugins.Deps

plugins {
    id("mpl-module-plugin")
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

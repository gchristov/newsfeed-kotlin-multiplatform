import com.gchristov.newsfeed.kmmgradleplugins.Deps

plugins {
    id("kmm-common-module-plugin")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.kmmCommonDi)
                api(Deps.Mvvm.liveData)
            }
        }
    }
}

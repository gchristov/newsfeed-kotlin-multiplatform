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
                api(libs.moko.mvvm.livedata)
            }
        }
    }
}

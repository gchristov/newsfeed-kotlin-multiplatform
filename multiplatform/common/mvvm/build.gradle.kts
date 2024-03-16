plugins {
    alias(libs.plugins.newsfeed.mpl.module)
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

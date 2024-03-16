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
        commonMain.dependencies {
            api(libs.moko.mvvm.livedata)
        }
    }
}

plugins {
    alias(libs.plugins.newsfeed.mpl.base)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.multiplatform.common.di"
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kodein)
            }
        }
    }
}

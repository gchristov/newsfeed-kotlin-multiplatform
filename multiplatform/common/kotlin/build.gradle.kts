plugins {
    alias(libs.plugins.newsfeed.mpl.base)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.multiplatform.common.kotlin"
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlinx.coroutines.core)
                api(libs.kotlinx.datetime)
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.kotlinx.coroutines.android)
            }
        }
    }
}

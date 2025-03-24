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
        commonMain.dependencies {
            api(libs.kodein)
            api(libs.touchlab.kermit)
            api(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.datetime)
            api(libs.kotlinx.serialization)
            api(libs.arrow.core)
        }
        androidMain.dependencies {
            api(libs.kotlinx.coroutines.android)
        }
    }
}

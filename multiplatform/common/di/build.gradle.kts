plugins {
    id("mpl-base-plugin")
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

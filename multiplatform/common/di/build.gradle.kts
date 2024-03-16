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
        commonMain.dependencies {
            api(libs.kodein)
        }
    }
}

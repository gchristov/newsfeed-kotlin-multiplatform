plugins {
    alias(libs.plugins.newsfeed.mpl.module)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.multiplatform.common.persistence"
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.sqlDelight.core)
            api(libs.multiplatformSettings)
        }
        androidMain.dependencies {
            implementation(libs.sqlDelight.android)
        }
        iosMain.dependencies {
            implementation(libs.sqlDelight.native)
        }
    }
}

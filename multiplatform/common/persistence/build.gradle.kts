plugins {
    id("mpl-module-plugin")
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.multiplatform.common.persistence"
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.sqlDelight.runtime)
                api(libs.multiplatformSettings)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.sqlDelight.android)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.sqlDelight.native)
            }
        }
    }
}

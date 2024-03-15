import com.gchristov.newsfeed.gradleplugins.Deps

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
                implementation(Deps.Multiplatform.SqlDelight.runtime)
                api(Deps.Multiplatform.SharedPreferences.multiplatformSettings)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Deps.Multiplatform.SqlDelight.driverAndroid)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Deps.Multiplatform.SqlDelight.driverNative)
            }
        }
    }
}

import com.gchristov.newsfeed.kmmgradleplugins.Deps

plugins {
    id("kmm-module-plugin")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.SqlDelight.runtime)
                api(Deps.SharedPreferences.multiplatformSettings)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Deps.SqlDelight.driverAndroid)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Deps.SqlDelight.driverNative)
            }
        }
    }
}

import com.gchristov.newsfeed.gradleplugins.Deps

plugins {
    id("mpl-base-plugin")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(Deps.Multiplatform.Kotlin.coroutinesCore)
                api(Deps.Multiplatform.Kotlin.dateTime)
            }
        }
        val androidMain by getting {
            dependencies {
                api(Deps.Multiplatform.Kotlin.coroutinesAndroid)
            }
        }
    }
}

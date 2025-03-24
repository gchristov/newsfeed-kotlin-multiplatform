plugins {
    alias(libs.plugins.newsfeed.mpl.module)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.multiplatform.common.firebase"
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.gitlive.firebase.firestore)
            api(libs.gitlive.firebase.analytics)
            implementation(libs.touchlab.kermit.crashlytics)
            implementation(libs.touchlab.crashkios)
        }
    }
}

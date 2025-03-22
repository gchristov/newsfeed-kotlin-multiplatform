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
            implementation(libs.kermit.crashlytics)
        }
    }
}

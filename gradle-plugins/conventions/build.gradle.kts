plugins {
    `kotlin-dsl`
}

gradlePlugin {

    // Android plugins
    plugins.register("android-base-plugin") {
        id = "android-base-plugin"
        implementationClass =
            "com.gchristov.newsfeed.gradleplugins.android.AndroidBasePlugin"
    }
    plugins.register("android-module-plugin") {
        id = "android-module-plugin"
        implementationClass =
            "com.gchristov.newsfeed.gradleplugins.android.AndroidModulePlugin"
    }
    plugins.register("android-compose-plugin") {
        id = "android-compose-plugin"
        implementationClass =
            "com.gchristov.newsfeed.gradleplugins.android.AndroidComposePlugin"
    }
    plugins.register("android-firebase-plugin") {
        id = "android-firebase-plugin"
        implementationClass =
            "com.gchristov.newsfeed.gradleplugins.android.AndroidFirebasePlugin"
    }
    plugins.register("android-feature-plugin") {
        id = "android-feature-plugin"
        implementationClass =
            "com.gchristov.newsfeed.gradleplugins.android.AndroidFeaturePlugin"
    }
    plugins.register("android-application-binary-plugin") {
        id = "android-application-binary-plugin"
        implementationClass =
            "com.gchristov.newsfeed.gradleplugins.android.AndroidApplicationBinaryPlugin"
    }

    // Multiplatform plugins
    plugins.register("mpl-base-plugin") {
        id = "mpl-base-plugin"
        implementationClass =
            "com.gchristov.newsfeed.gradleplugins.multiplatform.MplBasePlugin"
    }
    plugins.register("mpl-module-plugin") {
        id = "mpl-module-plugin"
        implementationClass =
            "com.gchristov.newsfeed.gradleplugins.multiplatform.MplModulePlugin"
    }
    plugins.register("mpl-data-plugin") {
        id = "mpl-data-plugin"
        implementationClass =
            "com.gchristov.newsfeed.gradleplugins.multiplatform.MplDataPlugin"
    }
    plugins.register("mpl-feature-plugin") {
        id = "mpl-feature-plugin"
        implementationClass =
            "com.gchristov.newsfeed.gradleplugins.multiplatform.MplFeaturePlugin"
    }
    plugins.register("mpl-build-config-plugin") {
        id = "mpl-build-config-plugin"
        implementationClass =
            "com.gchristov.newsfeed.gradleplugins.multiplatform.MplBuildConfigPlugin"
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    // Allows these to be available and applied in the pre-compiled conventions plugin
    implementation(libs.kotlin.serialization.gradlePlugin)
    implementation(libs.kotlin.compose.gradlePlugin)
    implementation(libs.kotlin.multiplatform.gradlePlugin)
    implementation(libs.buildKonfig.gradlePlugin)
    implementation(libs.sqlDelight.gradlePlugin)
}

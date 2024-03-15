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
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
}

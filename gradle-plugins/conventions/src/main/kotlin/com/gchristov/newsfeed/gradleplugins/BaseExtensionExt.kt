package com.gchristov.newsfeed.gradleplugins

import com.android.build.gradle.BaseExtension

internal fun BaseExtension.configureAndroid() {
    compileSdkVersion(34)
    defaultConfig {
        minSdk = 21
        targetSdk = 34
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    // Resolves "N files found with path 'META-INF/XXX'" errors
    packagingOptions {
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
    }
}
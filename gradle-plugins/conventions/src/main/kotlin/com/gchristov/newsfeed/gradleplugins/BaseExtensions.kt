package com.gchristov.newsfeed.gradleplugins

import com.android.build.gradle.BaseExtension

internal fun BaseExtension.configure() {
    compileSdkVersion(31)
    defaultConfig {
        minSdk = 21
        targetSdk = 31
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}
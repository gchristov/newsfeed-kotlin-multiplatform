package com.gchristov.newsfeed.gradleplugins.android

import com.gchristov.newsfeed.gradleplugins.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationBinaryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with (target) {
            with (plugins) {
                apply("com.android.application")
                apply(libs.findPlugin("newsfeed-android-base").get().get().pluginId)
            }
        }
    }
}
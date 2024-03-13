package com.gchristov.newsfeed.gradleplugins.android

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationBinaryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            plugins.apply("com.android.application")
            plugins.apply("android-base-plugin")
        }
    }
}
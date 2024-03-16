package com.gchristov.newsfeed.gradleplugins.android

import com.gchristov.newsfeed.gradleplugins.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with (target) {
            with (pluginManager) {
                apply("com.android.library")
                apply(libs.findPlugin("newsfeed-android-base").get().get().pluginId)
            }
            // Add dependencies after plugins are set to avoid missing "implementation" errors
            afterEvaluate {
                dependencies {
                    add("api", project(":android:common:test"))
                }
            }
        }
    }
}
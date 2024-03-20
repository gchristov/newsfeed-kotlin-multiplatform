package com.gchristov.newsfeed.gradleplugins.android

import com.gchristov.newsfeed.gradleplugins.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with (target) {
            with (plugins) {
                apply(libs.findPlugin("newsfeed-android-module").get().get().pluginId)
                apply(libs.findPlugin("newsfeed-android-compose").get().get().pluginId)
            }
            // Add dependencies after plugins are set to avoid missing "implementation" errors
            afterEvaluate {
                dependencies {
                    add("api", project(":android:common:navigation"))
                    add("api", project(":android:common:compose"))
                    add("api", project(":android:common:design"))
                    add("androidTestImplementation", project(":android:common:compose-test"))
                }
            }
        }
    }
}
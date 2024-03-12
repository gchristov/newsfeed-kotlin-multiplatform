package com.gchristov.newsfeed.gradleplugins.android

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            plugins.apply("com.android.library")
            plugins.apply("android-base-plugin")
            // Add dependencies after plugins are set to avoid missing "implementation" errors
            afterEvaluate {
                dependencies {
                    add("api", project(":common-test"))
                }
            }
        }
    }
}
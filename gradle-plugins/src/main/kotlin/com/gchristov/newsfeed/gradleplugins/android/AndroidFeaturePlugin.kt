package com.gchristov.newsfeed.gradleplugins.android

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            plugins.apply("android-module-plugin")
            plugins.apply("android-compose-plugin")
            // Add dependencies after plugins are set to avoid missing "implementation" errors
            afterEvaluate {
                dependencies {
                    add("api", project(":common-navigation"))
                    add("api", project(":common-compose"))
                    add("androidTestImplementation", project(":common-compose-test"))
                }
            }
        }
    }
}
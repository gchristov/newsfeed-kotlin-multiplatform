package com.gchristov.newsfeed.gradleplugins.android

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFirebasePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with (plugins) {
                apply("com.google.gms.google-services")
                apply("com.google.firebase.crashlytics")
            }
            // Add dependencies after plugins are set to avoid missing "implementation" errors
            afterEvaluate {
                dependencies {
                    add("api", project(":android:common:firebase"))
                }
            }
        }
    }
}
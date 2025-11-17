package com.gchristov.newsfeed.gradleplugins.android

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<BaseExtension> {
                buildFeatures.compose = true
                with (plugins) {
                    apply("org.jetbrains.kotlin.plugin.compose")
                }
            }
        }
    }
}
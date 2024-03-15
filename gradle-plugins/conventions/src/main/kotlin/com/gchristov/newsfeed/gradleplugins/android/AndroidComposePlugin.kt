package com.gchristov.newsfeed.gradleplugins.android

import com.android.build.gradle.BaseExtension
import com.gchristov.newsfeed.gradleplugins.Deps
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            extensions.configure<BaseExtension> {
                buildFeatures.compose = true
                composeOptions {
                    kotlinCompilerExtensionVersion = Deps.Android.Compose.compilerExtension
                }
            }
        }
    }
}
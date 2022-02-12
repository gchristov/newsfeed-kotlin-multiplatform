package com.gchristov.newsfeed.gradleplugins

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.configureComposeOptions()
    }
}

private fun Project.configureComposeOptions() {
    extensions.configure<BaseExtension> {
        buildFeatures.compose = true
        composeOptions {
            kotlinCompilerExtensionVersion = Deps.Compose.compilerExtension
        }
    }
}

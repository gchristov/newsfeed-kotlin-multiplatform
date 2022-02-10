package com.gchristov.newsfeed.gradleplugins

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class CommonComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.configureComposeOptions()
    }
}

private fun Project.configureComposeOptions() {
    plugins.apply("kotlin-kapt")
    extensions.configure<BaseExtension> {
        buildFeatures.compose = true
        composeOptions {
            kotlinCompilerExtensionVersion = Deps.Compose.compilerExtension
        }
    }
}

internal fun Project.configureCompose() {
    plugins.apply("common-compose-plugin")
    // Add dependencies after plugins are set to avoid missing "implementation" errors
    afterEvaluate {
        dependencies {
            add("implementation", project(":common-compose"))
            add("androidTestImplementation", project(":common-compose-test"))
        }
    }
}

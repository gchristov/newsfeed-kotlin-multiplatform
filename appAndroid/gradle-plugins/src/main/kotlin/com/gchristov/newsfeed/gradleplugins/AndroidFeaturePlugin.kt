package com.gchristov.newsfeed.gradleplugins

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeaturePlugin : AndroidModulePlugin() {
    override fun apply(target: Project) {
        super.apply(target)
        target.configureCompose()
        target.configureNavigation()
    }
}

private fun Project.configureNavigation() {
    // Add dependencies after plugins are set to avoid missing "implementation" errors
    afterEvaluate {
        dependencies {
            add("implementation", project(":common-navigation"))
        }
    }
}

private fun Project.configureCompose() {
    plugins.apply("android-compose-plugin")
    // Add dependencies after plugins are set to avoid missing "implementation" errors
    afterEvaluate {
        dependencies {
            add("implementation", project(":common-compose"))
            add("androidTestImplementation", project(":common-compose-test"))
        }
    }
}

package com.gchristov.newsfeed.gradleplugins

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CommonFeaturePlugin : CommonModulePlugin() {
    override fun apply(target: Project) {
        super.apply(target)
        target.configureCompose()
        target.configureNavigation()
        target.configureTests()
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

private fun Project.configureTests() {
    // Add dependencies after plugins are set to avoid missing "implementation" errors
    afterEvaluate {
        dependencies {
            add("implementation", project(":common-test"))
        }
    }
}

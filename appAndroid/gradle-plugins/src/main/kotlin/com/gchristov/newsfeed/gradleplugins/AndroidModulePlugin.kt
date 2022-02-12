package com.gchristov.newsfeed.gradleplugins

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

open class AndroidModulePlugin : AndroidLibraryPlugin() {
    override fun apply(target: Project) {
        super.apply(target)
        target.configureKotlin()
        target.configureTests()
    }
}

private fun Project.configureKotlin() {
    // Add dependencies after plugins are set to avoid missing "implementation" errors
    afterEvaluate {
        dependencies {
            add("implementation", project(":kmm-common-kotlin"))
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
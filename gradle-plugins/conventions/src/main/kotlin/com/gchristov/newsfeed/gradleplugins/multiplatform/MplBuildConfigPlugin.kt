package com.gchristov.newsfeed.gradleplugins.multiplatform

import org.gradle.api.Plugin
import org.gradle.api.Project

class MplBuildConfigPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            plugins.apply("com.codingfeline.buildkonfig")
        }
    }
}
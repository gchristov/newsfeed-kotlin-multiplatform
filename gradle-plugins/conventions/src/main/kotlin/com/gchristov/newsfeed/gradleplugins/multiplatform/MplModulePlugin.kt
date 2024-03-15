package com.gchristov.newsfeed.gradleplugins.multiplatform

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MplModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            plugins.apply("mpl-base-plugin")
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.maybeCreate("commonMain").dependencies {
                    api(project(":multiplatform:common:kotlin"))
                    api(project(":multiplatform:common:di"))
                    api(project(":multiplatform:common:test"))
                }
            }
        }
    }
}
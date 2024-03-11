package com.gchristov.newsfeed.gradleplugins.multiplatform

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MplDataPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            plugins.apply("mpl-module-plugin")
            plugins.apply("org.jetbrains.kotlin.plugin.serialization")
            plugins.apply("com.squareup.sqldelight")
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.maybeCreate("commonMain").dependencies {
                    api(project(":kmm-common-network"))
                    api(project(":kmm-common-persistence"))
                }
            }
        }
    }
}
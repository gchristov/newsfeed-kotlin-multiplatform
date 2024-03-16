package com.gchristov.newsfeed.gradleplugins.multiplatform

import com.gchristov.newsfeed.gradleplugins.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MplDataPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with (target) {
            with (plugins) {
                apply(libs.findPlugin("newsfeed-mpl-module").get().get().pluginId)
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("com.squareup.sqldelight")
            }
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.maybeCreate("commonMain").dependencies {
                    api(project(":multiplatform:common:network"))
                    api(project(":multiplatform:common:persistence"))
                }
            }
        }
    }
}
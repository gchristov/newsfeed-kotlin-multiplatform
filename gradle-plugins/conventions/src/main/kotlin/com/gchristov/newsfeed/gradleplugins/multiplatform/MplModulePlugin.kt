package com.gchristov.newsfeed.gradleplugins.multiplatform

import com.gchristov.newsfeed.gradleplugins.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MplModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with (target) {
            with (plugins) {
                apply(libs.findPlugin("newsfeed-mpl-base").get().get().pluginId)
            }
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
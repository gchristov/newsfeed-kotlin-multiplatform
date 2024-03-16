package com.gchristov.newsfeed.gradleplugins.multiplatform

import com.gchristov.newsfeed.gradleplugins.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MplFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with (target) {
            with (plugins) {
                apply(libs.findPlugin("newsfeed-mpl-module").get().get().pluginId)
            }
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.commonMain.dependencies {
                    api(project(":multiplatform:common:mvvm"))
                }
                sourceSets.commonTest.dependencies {
                    api(project(":multiplatform:common:mvvm-test"))
                }
            }
        }
    }
}
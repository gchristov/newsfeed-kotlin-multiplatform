package com.gchristov.newsfeed.gradleplugins.multiplatform

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MplFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            plugins.apply("mpl-module-plugin")
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.maybeCreate("commonMain").dependencies {
                    api(project(":kmm-common-mvvm"))
                }
                sourceSets.maybeCreate("commonTest").dependencies {
                    api(project(":kmm-common-mvvm-test"))
                }
            }
        }
    }
}
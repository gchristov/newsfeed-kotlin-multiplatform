package com.gchristov.newsfeed.gradleplugins.multiplatform

import com.android.build.gradle.BaseExtension
import com.gchristov.newsfeed.gradleplugins.configureAndroid
import com.gchristov.newsfeed.gradleplugins.configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MplBasePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with (target) {
            with (plugins) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.multiplatform")
            }
            configureKotlin()
            extensions.configure<BaseExtension> {
                configureAndroid()
            }
            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget()
                listOf(
                    iosX64(),
                    iosArm64(),
                    iosSimulatorArm64()
                )
            }
        }
    }
}
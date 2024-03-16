package com.gchristov.newsfeed.gradleplugins.multiplatform

import com.android.build.gradle.BaseExtension
import com.gchristov.newsfeed.gradleplugins.configure
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

class MplBasePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with (target) {
            with (plugins) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.multiplatform")
            }
            extensions.configure<BaseExtension> {
                configure()
                // Include AndroidManifest.xml, if it exists.
                sourceSets {
                    maybeCreate("main").manifest {
                        srcFile("src/androidMain/AndroidManifest.xml")
                    }
                }
                // Resolves "N files found with path 'META-INF/XXX'" errors
//                packagingOptions {
//                    resources.excludes.add("META-INF/AL2.0")
//                    resources.excludes.add("META-INF/LGPL2.1")
//                }
            }
            extensions.configure<KotlinMultiplatformExtension> {
                android()
                val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget = when {
                    System.getenv("SDK_NAME")?.startsWith("iphoneos") == true -> ::iosArm64
                    System.getenv("NATIVE_ARCH")?.startsWith("arm") == true -> ::iosSimulatorArm64
                    else -> ::iosX64
                }
                iosTarget("ios") {}
                // Allows libraries to not have to specify all source sets unless required.
                sourceSets.maybeCreate("commonMain")
                sourceSets.maybeCreate("androidMain")
                sourceSets.maybeCreate("iosMain")
            }
        }
    }
}
package com.gchristov.newsfeed.gradleplugins.android

import com.android.build.gradle.BaseExtension
import com.gchristov.newsfeed.gradleplugins.Deps
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidBasePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            // The base plugin is either an Android app or library, the former having higher precedence.
            if (!plugins.hasPlugin("com.android.application")) {
                plugins.apply("com.android.library")
            }
            plugins.apply("kotlin-android")
            plugins.apply("kotlin-parcelize")
            extensions.configure<BaseExtension> {
                compileSdkVersion(Deps.Android.compileSdk)
                defaultConfig {
                    minSdk = Deps.Android.minSdk
                    targetSdk = Deps.Android.targetSdk
                    testInstrumentationRunner = Deps.Android.Tests.testInstrumentationRunner
                }
                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = true
                    }
                }
                // Resolves "N files found with path 'META-INF/XXX'" errors
//                packagingOptions {
//                    resources.excludes.add("META-INF/AL2.0")
//                    resources.excludes.add("META-INF/LGPL2.1")
//                }
            }
        }
    }
}
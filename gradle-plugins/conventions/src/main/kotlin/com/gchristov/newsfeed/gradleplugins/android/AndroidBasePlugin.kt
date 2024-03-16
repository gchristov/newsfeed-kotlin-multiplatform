package com.gchristov.newsfeed.gradleplugins.android

import com.android.build.gradle.BaseExtension
import com.gchristov.newsfeed.gradleplugins.configure
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidBasePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with (target) {
            with (plugins) {
                // The base plugin is either an Android app or library, the former having higher precedence.
                if (!hasPlugin("com.android.application")) {
                    apply("com.android.library")
                }
                apply("kotlin-android")
                apply("kotlin-parcelize")
            }
            extensions.configure<BaseExtension> {
                configure()
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
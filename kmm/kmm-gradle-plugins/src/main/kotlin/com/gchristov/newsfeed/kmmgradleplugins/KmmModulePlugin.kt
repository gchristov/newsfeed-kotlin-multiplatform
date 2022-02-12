package com.gchristov.newsfeed.kmmgradleplugins

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

open class KmmModulePlugin : KmmPlatformPlugin() {
    override fun apply(target: Project) {
        super.apply(target)
        target.configureKotlin()
        target.configureDependencyInjection()
        target.configureTests()
    }
}

private fun Project.configureKotlin() {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.maybeCreate("commonMain").dependencies {
            api(project(":kmm-common-kotlin"))
        }
    }
}

private fun Project.configureDependencyInjection() {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.maybeCreate("commonMain").dependencies {
            api(project(":kmm-common-di"))
        }
    }
}

private fun Project.configureTests() {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.maybeCreate("commonMain").dependencies {
            api(project(":kmm-common-test"))
        }
    }
}

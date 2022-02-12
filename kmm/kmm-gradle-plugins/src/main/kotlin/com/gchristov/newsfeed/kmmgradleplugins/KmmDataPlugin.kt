package com.gchristov.newsfeed.kmmgradleplugins

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmmDataPlugin : KmmModulePlugin() {
    override fun apply(target: Project) {
        super.apply(target)
        target.configureNetwork()
        target.configurePersistence()
    }
}

private fun Project.configureNetwork() {
    plugins.apply("org.jetbrains.kotlin.plugin.serialization")
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.maybeCreate("commonMain").dependencies {
            implementation(project(":kmm-common-network"))
        }
    }
}

private fun Project.configurePersistence() {
    plugins.apply("com.squareup.sqldelight")
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.maybeCreate("commonMain").dependencies {
            implementation(project(":kmm-common-persistence"))
        }
    }
}

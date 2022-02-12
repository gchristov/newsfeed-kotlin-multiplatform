package com.gchristov.newsfeed.kmmgradleplugins

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmmFeaturePlugin : KmmModulePlugin() {
    override fun apply(target: Project) {
        super.apply(target)
        target.configureMvvm()
    }
}

private fun Project.configureMvvm() {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.maybeCreate("commonMain").dependencies {
            api(project(":kmm-common-mvvm"))
        }
        sourceSets.maybeCreate("commonTest").dependencies {
            implementation(project(":kmm-common-mvvm-test"))
        }
    }
}

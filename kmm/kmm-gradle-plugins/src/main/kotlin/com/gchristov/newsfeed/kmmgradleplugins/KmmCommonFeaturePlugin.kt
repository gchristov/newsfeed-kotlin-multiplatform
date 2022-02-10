package com.gchristov.newsfeed.kmmgradleplugins

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmmCommonFeaturePlugin : KmmCommonModulePlugin() {
    override fun apply(target: Project) {
        super.apply(target)
        target.configureKotlin()
        target.configureDependencyInjection()
        target.configureTests()
        target.configureMvvm()
    }
}

internal fun Project.configureKotlin() {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.maybeCreate("commonMain").dependencies {
            api(project(":kmm-common-kotlin"))
        }
    }
}

internal fun Project.configureDependencyInjection() {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.maybeCreate("commonMain").dependencies {
            api(project(":kmm-common-di"))
        }
    }
}

internal fun Project.configureTests() {
    extensions.configure<KotlinMultiplatformExtension> {
        sourceSets.maybeCreate("commonMain").dependencies {
            api(project(":kmm-common-test"))
        }
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

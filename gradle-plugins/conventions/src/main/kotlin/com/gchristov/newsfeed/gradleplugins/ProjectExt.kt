package com.gchristov.newsfeed.gradleplugins

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.file.Directory
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.Properties

internal val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.configureKotlin() {
    // Use withType to workaround https://youtrack.jetbrains.com/issue/KT-55947
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
}

fun Project.binaryRootDirectory(): Directory = layout.buildDirectory.dir("dist/js").get()

fun Project.envSecret(key: String): String {
    val propFile = file("./secrets.properties")
    val properties = Properties()
    properties.load(FileInputStream(propFile))
    val property = properties.getProperty(key)
    if (property.isNullOrBlank()) {
        throw IllegalStateException("Required property is missing: property=$key")
    }
    return property
}
plugins {
    // Normally, this would be libs.plugins.newsfeed.mpl.module but since this is an umbrella module this is enough
    alias(libs.plugins.newsfeed.mpl.base)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.multiplatform.umbrella"
    }
}

kotlin {
    /*
     Dependencies that should be exported to native clients (e.g. iOS) to avoid having their module
     name added as prefix to their class names. More info here:
     https://touchlab.co/multiple-kotlin-frameworks-in-application/
     */
    val exportedDependencies = listOf(
        projects.multiplatform.common.kotlin,
        projects.multiplatform.common.test,
        projects.multiplatform.feed.feature,
        projects.multiplatform.feed.data,
        projects.multiplatform.feed.testFixtures,
        projects.multiplatform.post.feature,
        projects.multiplatform.post.data,
        projects.multiplatform.post.testFixtures,
    )

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "NewsfeedMultiplatform"
            // Toggling this to false will cause missing FirebaseCore errors
            isStatic = true
            exportedDependencies.forEach { dependency -> export(dependency) }
        }
    }

    sourceSets {
        commonMain.dependencies {
            exportedDependencies.forEach { api(it) }
            implementation(libs.kotlinx.coroutines.core) // Needed for coroutine dispatchers
        }
    }
}
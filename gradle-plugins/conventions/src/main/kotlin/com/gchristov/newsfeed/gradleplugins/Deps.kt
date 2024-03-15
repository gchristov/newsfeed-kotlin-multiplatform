package com.gchristov.newsfeed.gradleplugins

object Deps {
    object Android {
        const val minSdk = 21
        const val targetSdk = 31
        const val compileSdk = 31

        object Accompanist {
            const val swipeRefresh = "com.google.accompanist:accompanist-swiperefresh:0.19.0"
        }

        object Compose {
            private const val composeVersion = "1.1.0-rc03"
            const val liveData = "androidx.compose.runtime:runtime-livedata:$composeVersion"
            const val compiler = "androidx.compose.compiler:compiler:$composeVersion"
            const val compilerExtension = composeVersion
            const val activity = "androidx.activity:activity-compose:1.3.1"
            const val uiTooling = "androidx.compose.ui:ui-tooling:$composeVersion"
            const val foundation = "androidx.compose.foundation:foundation:$composeVersion"
            const val foundationLayout = "androidx.compose.foundation:foundation-layout:$composeVersion"
            const val material = "androidx.compose.material:material:$composeVersion"
            const val materialIcons = "androidx.compose.material:material-icons-core:$composeVersion"
            const val uiTestJunit = "androidx.compose.ui:ui-test-junit4:$composeVersion"
            const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:$composeVersion"
            const val coil = "io.coil-kt:coil-compose:1.4.0"
            const val html = "com.github.ireward:compose-html:1.0.1"
        }

        object Tests {
            const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            const val junit = "junit:junit:4.13.2"
        }
    }

    object Multiplatform {
        object Kodein {
            const val di = "org.kodein.di:kodein-di:7.9.0"
        }

        object Kotlin {
            private const val coroutinesVersion = "1.6.0"
            // "-native-mt" is required here, otherwise iOS fails with runtime exception
            const val coroutinesCore =
                "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion-native-mt"
            const val coroutinesAndroid =
                "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
            const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:0.3.2"
        }

        object Mvvm {
            private const val mvvmVersion = "0.12.0"
            const val liveData = "dev.icerock.moko:mvvm-livedata:$mvvmVersion"
            const val test = "dev.icerock.moko:mvvm-test:$mvvmVersion"
        }

        object Ktor {
            private const val ktorVersion = "1.6.7"
            const val clientCore = "io.ktor:ktor-client-core:$ktorVersion"
            const val clientSerialization = "io.ktor:ktor-client-serialization:$ktorVersion"
            const val clientLogging = "io.ktor:ktor-client-logging:$ktorVersion"
            const val clientAndroid = "io.ktor:ktor-client-android:$ktorVersion"
            const val clientIos = "io.ktor:ktor-client-ios:$ktorVersion"
            const val logbackClassic = "ch.qos.logback:logback-classic:1.2.10"
        }

        object SqlDelight {
            private const val sqlDelightVersion = "1.5.3"
            const val runtime = "com.squareup.sqldelight:runtime:$sqlDelightVersion"
            const val driverAndroid = "com.squareup.sqldelight:android-driver:$sqlDelightVersion"
            const val driverNative = "com.squareup.sqldelight:native-driver:$sqlDelightVersion"
        }

        object SharedPreferences {
            const val multiplatformSettings = "com.russhwolf:multiplatform-settings:0.8.1"
        }

        object Tests {
            // Assertions for use in common code
            const val testCommon = "test-common"

            // Test annotations for use in common code
            const val testCommonAnnotations = "test-annotations-common"

            /*
            Provides an implementation of Asserter on top of JUnit and maps the test
            annotations from kotlin-test-annotations-common to JUnit test annotations
             */
            const val testJunit = "test-junit"
            const val junit = "junit:junit:4.13.2"

            // Used to set a test instrumentation runner for Android
            const val junitRunner = "androidx.test:runner:1.4.0"

            // Used for InstantTaskExecutorRule
            const val archCoreTesting = "androidx.arch.core:core-testing:2.1.0"
        }
    }
}
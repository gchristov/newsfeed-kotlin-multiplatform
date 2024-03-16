plugins {
    alias(libs.plugins.newsfeed.mpl.base)
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.multiplatform.common.test"
    }
}

kotlin {
    /*
    This module is used in other test modules. Common dependencies are linked to the 'main'
    source sets, and marked as `api`, rather than 'test'. This is because 'test' source-specific
    dependencies and code are local to the relevant module and cannot be accesses by other modules.
     */
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core) // Needed for FakeResponse
                implementation(libs.kotlinx.datetime) // Needed for FakeClock
                api(kotlin("test-common")) // Assertions for use in common code
                api(kotlin("test-annotations-common")) // Test annotations for use in common code
            }
        }
        val androidMain by getting {
            dependencies {
                // Provides an implementation of Asserter on top of JUnit and maps the test
                // annotations from kotlin-test-annotations-common to JUnit test annotations
                api(kotlin("test-junit"))
                api(libs.junit)
                api(libs.androidx.testRunner)
                api(libs.androidx.coreTesting)
            }
        }
    }
}

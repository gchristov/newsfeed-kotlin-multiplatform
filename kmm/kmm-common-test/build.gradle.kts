import com.gchristov.newsfeed.gradleplugins.Deps

plugins {
    id("mpl-base-plugin")
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
                implementation(Deps.Multiplatform.Kotlin.coroutinesCore) // Needed for FakeResponse
                implementation(Deps.Multiplatform.Kotlin.dateTime) // Needed for FakeClock
                api(kotlin(Deps.Multiplatform.Tests.testCommon))
                api(kotlin(Deps.Multiplatform.Tests.testCommonAnnotations))
            }
        }
        val androidMain by getting {
            dependencies {
                api(kotlin(Deps.Multiplatform.Tests.testJunit))
                api(Deps.Multiplatform.Tests.junit)
                api(Deps.Multiplatform.Tests.junitRunner)
                api(Deps.Multiplatform.Tests.archCoreTesting)
            }
        }
    }
}

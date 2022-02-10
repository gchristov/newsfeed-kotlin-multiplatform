import com.gchristov.newsfeed.kmmgradleplugins.Deps

plugins {
    id("kmm-common-module-plugin")
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
                implementation(Deps.Kotlin.coroutinesCore)
                api(kotlin(Deps.Tests.testCommon))
                api(kotlin(Deps.Tests.testCommonAnnotations))
            }
        }
        val androidMain by getting {
            dependencies {
                api(kotlin(Deps.Tests.testJunit))
                api(Deps.Tests.junit)
                api(Deps.Tests.junitRunner)
                api(Deps.Tests.archCoreTesting)
            }
        }
    }
}

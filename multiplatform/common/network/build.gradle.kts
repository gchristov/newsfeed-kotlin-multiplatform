val packageId = "com.gchristov.newsfeed.multiplatform.common.network"

plugins {
    id("mpl-module-plugin")
    id("com.codingfeline.buildkonfig")
}

android {
    defaultConfig {
        namespace = "com.gchristov.newsfeed.multiplatform.common.network"
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ktor.client.core)
                api(libs.ktor.client.serialization)
                implementation(libs.ktor.client.logging)
                implementation(libs.logback)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.android)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.ios)
            }
        }
    }
}

buildkonfig {
    packageName = packageId
    defaultConfigs {
        buildConfigField(
            type = com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            name = "API_KEY",
            value = "86cb30cc-1eb4-478f-a147-f73e02862a2e"
        )
        buildConfigField(
            type = com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            name = "API_URL",
            value = "https://content.guardianapis.com"
        )
        buildConfigField(
            type = com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            name = "API_AUTH_HEADER",
            value = "api-key"
        )
    }
}

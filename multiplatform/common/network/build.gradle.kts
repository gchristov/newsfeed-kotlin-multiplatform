val packageId = "com.gchristov.newsfeed.multiplatform.common.network"

plugins {
    alias(libs.plugins.newsfeed.mpl.module)
    id("com.codingfeline.buildkonfig")
}

android {
    defaultConfig {
        namespace = packageId
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.ktor.client.core)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.client.serializationJson)
            implementation(libs.ktor.client.logging)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
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

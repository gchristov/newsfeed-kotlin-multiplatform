import com.gchristov.newsfeed.gradleplugins.envSecret

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
    exposeObjectWithName = "BuildConfig"
    defaultConfigs {
        buildConfigField(
            type = com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            name = "GUARDIAN_API_KEY",
            value = project.envSecret("GUARDIAN_API_KEY")
        )
        buildConfigField(
            type = com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            name = "GUARDIAN_API_URL",
            value = project.envSecret("GUARDIAN_API_URL")
        )
    }
}

import com.gchristov.newsfeed.gradleplugins.Deps

val packageId = "com.gchristov.newsfeed.kmmcommonnetwork"

plugins {
    id("mpl-module-plugin")
    id("com.codingfeline.buildkonfig")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.Multiplatform.Ktor.clientCore)
                api(Deps.Multiplatform.Ktor.clientSerialization)
                implementation(Deps.Multiplatform.Ktor.clientLogging)
                implementation(Deps.Multiplatform.Ktor.logbackClassic)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Deps.Multiplatform.Ktor.clientAndroid)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Deps.Multiplatform.Ktor.clientIos)
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

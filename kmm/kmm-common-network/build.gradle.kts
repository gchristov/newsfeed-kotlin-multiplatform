import com.gchristov.newsfeed.kmmgradleplugins.Deps

val packageId = "com.gchristov.newsfeed.kmmcommonnetwork"

plugins {
    id("kmm-module-plugin")
    id("com.codingfeline.buildkonfig")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.Ktor.clientCore)
                api(Deps.Ktor.clientSerialization)
                implementation(Deps.Ktor.clientLogging)
                implementation(Deps.Ktor.logbackClassic)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Deps.Ktor.clientAndroid)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Deps.Ktor.clientIos)
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

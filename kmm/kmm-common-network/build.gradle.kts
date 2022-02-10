import com.gchristov.newsfeed.kmmgradleplugins.Deps

val packageId = "com.gchristov.newsfeed.kmmcommonnetwork"

plugins {
    id("kmm-common-module-plugin")
    id("com.codingfeline.buildkonfig")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.kmmCommonDi)
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
            value = "Z2VvcmdpOnBhc3N3b3Jk"
        )
        buildConfigField(
            type = com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            name = "API_URL",
            value = "https://peanut-exercice-android.herokuapp.com"
        )
    }
}

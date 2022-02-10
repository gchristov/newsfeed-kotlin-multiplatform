plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins.register("common-application-plugin") {
        id = "common-application-plugin"
        implementationClass = "com.gchristov.newsfeed.gradleplugins.CommonApplicationPlugin"
    }
    plugins.register("common-module-plugin") {
        id = "common-module-plugin"
        implementationClass = "com.gchristov.newsfeed.gradleplugins.CommonModulePlugin"
    }
    plugins.register("common-compose-plugin") {
        id = "common-compose-plugin"
        implementationClass = "com.gchristov.newsfeed.gradleplugins.CommonComposePlugin"
    }
    plugins.register("common-feature-plugin") {
        id = "common-feature-plugin"
        implementationClass = "com.gchristov.newsfeed.gradleplugins.CommonFeaturePlugin"
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.0.3")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
}
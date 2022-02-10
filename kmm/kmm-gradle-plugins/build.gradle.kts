plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins.register("kmm-common-module-plugin") {
        id = "kmm-common-module-plugin"
        implementationClass = "com.gchristov.newsfeed.kmmgradleplugins.KmmCommonModulePlugin"
    }
    plugins.register("kmm-common-data-plugin") {
        id = "kmm-common-data-plugin"
        implementationClass = "com.gchristov.newsfeed.kmmgradleplugins.KmmCommonDataPlugin"
    }
    plugins.register("kmm-common-feature-plugin") {
        id = "kmm-common-feature-plugin"
        implementationClass = "com.gchristov.newsfeed.kmmgradleplugins.KmmCommonFeaturePlugin"
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
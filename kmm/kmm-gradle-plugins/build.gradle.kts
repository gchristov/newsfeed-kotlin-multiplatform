plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins.register("kmm-platform-plugin") {
        id = "kmm-platform-plugin"
        implementationClass = "com.gchristov.newsfeed.kmmgradleplugins.KmmPlatformPlugin"
    }
    plugins.register("kmm-module-plugin") {
        id = "kmm-module-plugin"
        implementationClass = "com.gchristov.newsfeed.kmmgradleplugins.KmmModulePlugin"
    }
    plugins.register("kmm-data-plugin") {
        id = "kmm-data-plugin"
        implementationClass = "com.gchristov.newsfeed.kmmgradleplugins.KmmDataPlugin"
    }
    plugins.register("kmm-feature-plugin") {
        id = "kmm-feature-plugin"
        implementationClass = "com.gchristov.newsfeed.kmmgradleplugins.KmmFeaturePlugin"
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.1.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
}
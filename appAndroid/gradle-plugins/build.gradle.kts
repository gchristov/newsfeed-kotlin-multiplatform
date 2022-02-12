plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins.register("android-application-plugin") {
        id = "android-application-plugin"
        implementationClass = "com.gchristov.newsfeed.gradleplugins.AndroidApplicationPlugin"
    }
    plugins.register("android-library-plugin") {
        id = "android-library-plugin"
        implementationClass = "com.gchristov.newsfeed.gradleplugins.AndroidLibraryPlugin"
    }
    plugins.register("android-module-plugin") {
        id = "android-module-plugin"
        implementationClass = "com.gchristov.newsfeed.gradleplugins.AndroidModulePlugin"
    }
    plugins.register("android-compose-plugin") {
        id = "android-compose-plugin"
        implementationClass = "com.gchristov.newsfeed.gradleplugins.AndroidComposePlugin"
    }
    plugins.register("android-feature-plugin") {
        id = "android-feature-plugin"
        implementationClass = "com.gchristov.newsfeed.gradleplugins.AndroidFeaturePlugin"
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
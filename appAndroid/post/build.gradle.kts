plugins {
    id("common-feature-plugin")
}

dependencies {
    api(projects.kmmPost)
    androidTestImplementation(projects.postTestFixtures)
}
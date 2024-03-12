/**
 * Prefix KMM modules with 'kmm-'.
 * Suffix Gradle plugin modules with '-plugins'.
 */
val projects = listOf(
    "kmm-umbrella",
    "appAndroid",
)

/**
 * Android modules are under the 'appAndroid' directory.
 * KMM modules are under the 'kmm' directory.
 */
projects.forEach { project ->
    if (project.isGradlePlugin()) {
        includeBuild(project.projectDir())
    } else {
        include(":$project")
        project(":$project").projectDir = java.io.File(project.projectDir())
    }
}

fun String.projectRoot() = if (startsWith("kmm-")) "kmm" else "appAndroid"

fun String.projectDir() = "${projectRoot()}/$this"

fun String.isGradlePlugin() = endsWith("-plugins")
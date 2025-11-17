// Declare the gradle plugins but do not explicitly include them in every module. Sub-modules
// decide whether to include the actual plugin if needed.
plugins {
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.kotlin.android).apply(false)
}
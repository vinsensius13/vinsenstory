// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false // Plugin untuk aplikasi Android
    alias(libs.plugins.jetbrains.kotlin.android) apply false // Plugin untuk Kotlin Android
    alias(libs.plugins.hilt.android) apply false // Plugin untuk Hilt
}

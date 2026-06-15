buildscript {
    dependencies {
        
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint) apply false
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    // Configure detekt
    configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        buildUponDefaultConfig = true
        allRules = false
    }

    // Configure ktlint
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        android.set(true)
        outputToConsole.set(true)
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-Amoshi.generateAdapter.kapt=false")
    }
}
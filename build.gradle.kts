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
    id("com.google.gms.google-services") version "4.4.0" apply false
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    // Configure detekt
    configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        buildUponDefaultConfig = true
        allRules = false
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))

        // Pre-existing findings are recorded per module in detekt-baseline.xml so the build fails
        // on *new* violations while the existing backlog stays visible and diffable. A baseline is
        // a debt ledger, not a fix: see docs/roadmap.md §1.8 for the counts and burn-down order.
        // Regenerate deliberately with `./gradlew detektBaseline`, never to clear a fresh warning.
        val moduleBaseline = file("detekt-baseline.xml")
        if (moduleBaseline.exists()) {
            baseline = moduleBaseline
        }
    }

    // Configure ktlint
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        android.set(true)
        outputToConsole.set(true)
    }
}
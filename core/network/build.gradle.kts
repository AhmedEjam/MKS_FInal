plugins {
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.android.library)
    // alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
}

apply(plugin = "com.google.dagger.hilt.android")

android {
    namespace = "com.ahmedyejam.mks.core.network"
    compileSdk = 35
    defaultConfig { minSdk = 30 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
dependencies {
    implementation(project(":core:model"))
    implementation(libs.okhttp)
    implementation(libs.moshi.kotlin)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}

kotlin {
    jvmToolchain(11)
}

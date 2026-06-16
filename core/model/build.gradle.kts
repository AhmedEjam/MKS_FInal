plugins {
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.android.library)
    id("com.google.devtools.ksp")
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.ahmedyejam.mks.core.model"
    compileSdk = 37
    defaultConfig { minSdk = 30 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
    }
}
ksp {
    arg("moshi.generateAdapter.kapt", "false")
}
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)
    implementation(libs.kotlinx.serialization.json)
}

kotlin {
    jvmToolchain(11)
}

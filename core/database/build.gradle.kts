plugins {
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.android.library)
    // alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
}

apply(plugin = "com.google.dagger.hilt.android")

android {
    namespace = "com.ahmedyejam.mks.core.database"
    compileSdk = 35
    defaultConfig { minSdk = 30 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    sourceSets {
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }
}
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
}
dependencies {
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    "ksp"(libs.androidx.room.compiler)
    implementation(project(":core:model"))
    implementation(libs.moshi.kotlin)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.core)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}

kotlin {
    jvmToolchain(11)
}

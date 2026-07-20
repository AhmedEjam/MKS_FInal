plugins {
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.android.library)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.ahmedyejam.mks.core.data"
    compileSdk = 37
    defaultConfig { minSdk = 30 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(libs.kotlinx.coroutines.core)

    // Preferences
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.room.runtime)

    // Tests   // File Parsing
    implementation(libs.poi)
    implementation(libs.poi.ooxml)
    implementation(libs.zip4j)

    // Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.moshi)

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.okhttp)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation("io.mockk:mockk:1.14.11")
    testImplementation("org.robolectric:robolectric:4.13")
    testImplementation("androidx.test:core:1.6.1")
    testImplementation("androidx.test.ext:junit:1.2.1")
    testImplementation(libs.androidx.room.testing)
    testImplementation(project(":core:database"))
}

kotlin {
    jvmToolchain(11)
}

plugins {
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
}


android {
    namespace = "com.ahmedyejam.mks.core.data"
    compileSdk = 35
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
    implementation(libs.moshi.kotlin)
    
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.okhttp)
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    implementation("javax.inject:javax.inject:1")
}

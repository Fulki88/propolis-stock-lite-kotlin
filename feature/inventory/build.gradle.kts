plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
}


android {
    namespace = "feature.inventory"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
        buildConfig = false
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "${project.findProperty("composeCompiler")}"
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}


dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))

    implementation(platform("androidx.compose:compose-bom:${project.findProperty("composeBom")}"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:${project.findProperty("material3Version")}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${project.findProperty("lifecycleRuntimeKtx")}")
    implementation("androidx.activity:activity-compose:${project.findProperty("activityComposeVersion")}")
    implementation("androidx.hilt:hilt-navigation-compose:${project.findProperty("hiltNavCompose")}")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation("com.google.dagger:hilt-android:${project.findProperty("hiltVersion")}")
    kapt("com.google.dagger:hilt-android-compiler:${project.findProperty("hiltVersion")}")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${project.findProperty("kotlinxSerializationJson")}")
    
    // Retrofit for API calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}

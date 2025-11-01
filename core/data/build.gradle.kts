plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
}


android {
    namespace = "id.firobusiness.propolisstocklite.core.data"
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

    implementation("androidx.datastore:datastore-preferences:${project.findProperty("datastoreVersion")}")

    implementation("androidx.room:room-runtime:${project.findProperty("roomVersion")}")
    implementation("androidx.room:room-ktx:${project.findProperty("roomVersion")}")
    kapt("androidx.room:room-compiler:${project.findProperty("roomVersion")}")

    implementation("com.google.dagger:hilt-android:${project.findProperty("hiltVersion")}")
    kapt("com.google.dagger:hilt-android-compiler:${project.findProperty("hiltVersion")}")

    implementation(platform("androidx.compose:compose-bom:${project.findProperty("composeBom")}"))
    implementation("androidx.compose.ui:ui")
}

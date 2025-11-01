plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}


android {
    namespace = "id.firobusiness.propolisstocklite.core.designsystem"
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
    implementation(platform("androidx.compose:compose-bom:${project.findProperty("composeBom")}"))
    implementation("androidx.core:core-ktx:${project.findProperty("coreKtx")}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${project.findProperty("lifecycleRuntimeKtx")}")
    implementation("androidx.activity:activity-compose:${project.findProperty("activityComposeVersion")}")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:${project.findProperty("material3Version")}")
    debugImplementation("androidx.compose.ui:ui-tooling")
}


plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.kapt")
}


android {
    namespace = "id.firobusiness.propolisstocklite"
    compileSdk = 35

    defaultConfig {
        applicationId = "id.firobusiness.propolisstocklite"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        compose = true
        buildConfig = true
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
    packaging {
        resources {
            excludes += setOf("/META-INF/{AL2.0}", "/META-INF/{LGPL2.1}")
        }
    }
}


dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))
    implementation(project(":feature:inventory"))
    implementation(project(":feature:sales"))

    implementation(platform("androidx.compose:compose-bom:${project.findProperty("composeBom")}"))
    implementation("androidx.core:core-ktx:${project.findProperty("coreKtx")}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${project.findProperty("lifecycleRuntimeKtx")}")
    implementation("androidx.activity:activity-compose:${project.findProperty("activityComposeVersion")}")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:${project.findProperty("material3Version")}")
    implementation("androidx.navigation:navigation-compose:${project.findProperty("navigationCompose")}")
    implementation("androidx.hilt:hilt-navigation-compose:${project.findProperty("hiltNavCompose")}")

    implementation("com.google.dagger:hilt-android:${project.findProperty("hiltVersion")}")
    kapt("com.google.dagger:hilt-android-compiler:${project.findProperty("hiltVersion")}")
    debugImplementation("androidx.compose.ui:ui-tooling")
}

buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:${hiltVersion}")
    }
}

plugins {
    id("com.android.application") version "${agpVersion}" apply false
    id("com.android.library") version "${agpVersion}" apply false
    id("org.jetbrains.kotlin.android") version "${kotlinVersion}" apply false
    id("org.jetbrains.kotlin.kapt") version "${kotlinVersion}" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "${kotlinVersion}" apply false
}

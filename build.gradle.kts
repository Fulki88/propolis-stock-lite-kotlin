buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:${project.findProperty("hiltVersion")}")
    }
}

plugins {
    id("com.android.application") version "${project.findProperty("agpVersion")}" apply false
    id("com.android.library") version "${project.findProperty("agpVersion")}" apply false
    id("org.jetbrains.kotlin.android") version "${project.findProperty("kotlinVersion")}" apply false
    id("org.jetbrains.kotlin.kapt") version "${project.findProperty("kotlinVersion")}" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "${project.findProperty("kotlinVersion")}" apply false
}

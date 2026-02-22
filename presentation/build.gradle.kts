plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
    alias(libs.plugins.compose)
    kotlin("kapt")
}

android {
    namespace = "com.example.presentation"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.presentation"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = compileSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions.add("environment")

    productFlavors {
        create("prod") {
            applicationIdSuffix = ".prod"
            isDefault = true
        }
        create("mock") {
            applicationIdSuffix = ".mock"
        }
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinGradlePlugin.get()
    }

    kotlinOptions {
        freeCompilerArgs += listOf("-Xjvm-default=all")
    }
}

kotlin {
    jvmToolchain(libs.versions.jdk.get().toInt())
}

dependencies {
    api(project(":data"))
    testImplementation(project(":core-test"))
    debugImplementation(libs.chucker.debug)
    releaseImplementation(libs.chucker.release)

    // Kover Coverage (All Modules)
    kover(project(":data"))
    kover(project(":domain"))

    // Kotlin
    implementation(libs.kotlin.stdlib.jdk8)

    // AndroidX
    implementation(libs.appcompat)
    implementation(libs.lifecycle.viewmodel.ktx)

    // Extensions
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.extensions)

    // okHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // GSON
    implementation(libs.gson)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.retrofit2.kotlin.coroutines.adapter)

    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    // Hilt
    api(libs.hilt.dagger.android)
    api(libs.hilt.work)
    kapt(libs.hilt.dagger.compiler)
    kapt(libs.hilt.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Paging
    implementation(libs.paging.compose)

    // WorkManager
    implementation(libs.work.runtime.ktx)

    // Palette
    implementation(libs.palette)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.ui.util)
    implementation(libs.compose.animation)
    implementation(libs.compose.runtime)
    implementation(libs.compose.runtime.saveable)
    implementation(libs.compose.material3)
    implementation(libs.compose.material)
    implementation(libs.compose.material.icons.extended)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.wheel.picker.compose)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Navigation
    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.navigation.runtime)
    implementation(libs.navigation.ui)
    implementation(libs.navigation.viewmodel)
    implementation(libs.navigation.adaptive)

    //media3
    implementation(libs.bundles.media3)

    // Serialization
    implementation(libs.kotlinx.serialization.json)
}
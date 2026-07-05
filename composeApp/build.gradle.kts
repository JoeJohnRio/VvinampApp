import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose)
}

kotlin {
    androidLibrary {
        namespace = "com.example.vvinamp"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "composeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":domain"))
            implementation(project(":data"))
            implementation(project(":graphql"))

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            
            implementation(libs.lifecycle.runtime.compose)

            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.paging.common)

            implementation(libs.jetbrain.navigation.ui)

            implementation(libs.coil.compose)

            implementation(libs.kotlinx.coroutines.core)
        }
        
        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.accompanist.systemuicontroller)
            implementation(libs.palette)
            implementation(libs.bundles.media3)

            // Paging Compose (Currently Android only)
            implementation(libs.paging.compose)
            
            // UI Components (Moved to Android only as they might not be KMP ready)
            implementation(libs.reorderable)
            implementation(libs.wheel.picker.compose)
            
            // ViewModel Compose (Android specific parts if needed)
            implementation(libs.lifecycle.viewmodel.compose)

            // Network (Android-specific parts used in DI)
            implementation(libs.retrofit)
            implementation(libs.converter.gson)
            implementation(libs.okhttp)
            implementation(libs.logging.interceptor)
            implementation(libs.chucker.debug)
            
            // Image Loading
            implementation(libs.coil.network.okhttp)
        }
    }
}

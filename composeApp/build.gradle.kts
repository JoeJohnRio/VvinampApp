import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.serialization)
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
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            
            implementation(libs.lifecycle.runtime.compose)

            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.paging.common)

            implementation(libs.navigation3.ui)
            implementation(libs.coil.compose)

            implementation(libs.kotlinx.coroutines.core)
        }
        
        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.accompanist.systemuicontroller)
            implementation(libs.palette)
            implementation(libs.bundles.media3)

            implementation(libs.constraintlayout.compose)
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

compose.resources {
    publicResClass = true
    generateResClass = always
    packageOfResClass = "vvinampapp.composeapp.generated.resources"
}

// Workaround: Compose Multiplatform's Android task pipeline doesn't include
// commonMain resources when using com.android.kotlin.multiplatform.library
// plugin (AGP 9.0). Expose the prepared commonMain resources at a known
// location so the consuming app (`presentation`) can include them as assets.
val copyComposeResourcesToAndroidAssets by tasks.registering(Copy::class) {
    dependsOn("prepareComposeResourcesTaskForCommonMain")
    from(layout.buildDirectory.dir("generated/compose/resourceGenerator/preparedResources/commonMain"))
    into(layout.buildDirectory.dir("generated/composeResourcesForAndroid/assets"))
}

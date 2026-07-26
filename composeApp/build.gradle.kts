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

//    wasmJs {
//        browser()
//        binaries.executable()
//    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":domain"))
            implementation(project(":data"))
            implementation(project(":graphql"))

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material)
            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.compose.ui)
            implementation(libs.compose.resources)
            implementation(libs.compose.ui.tooling)
            
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            
            implementation(libs.lifecycle.runtime.compose)

            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.paging.common)
            implementation(libs.paging.compose)

            implementation(libs.navigation3.ui)
            implementation(libs.coil.compose)

            implementation(libs.kotlinx.coroutines.core)
        }
        
        androidMain.dependencies {
            implementation("org.jetbrains.compose.ui:ui-tooling:${libs.versions.composeMultiplatform.get()}")
            implementation(libs.koin.android)
            implementation(libs.accompanist.systemuicontroller)
            implementation(libs.palette)
            implementation(libs.bundles.media3)

            implementation(libs.constraintlayout.compose)
            
            // UI Components (Moved to Android only as they might not be KMP ready)
            implementation(libs.reorderable)
            implementation(libs.wheel.picker.compose)
            
            // ViewModel Compose (Android specific parts if needed)
            implementation(libs.lifecycle.viewmodel.compose)

            // Network (Android-specific parts used in DI)
            implementation(libs.okhttp)
            implementation(libs.logging.interceptor)
            implementation(libs.chucker.debug)
            
            // Image Loading
            implementation(libs.coil.network.okhttp)
        }

//        val wasmJsMain by getting {
//            dependencies {
//                implementation(libs.ktor.client.js)
//            }
//        }
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
    from(layout.buildDirectory.dir("generated/compose/resourceGenerator/preparedResources/commonMain/composeResources"))
    into(layout.buildDirectory.dir("generated/composeResourcesForAndroid/assets/composeResources/vvinampapp.composeapp.generated.resources"))
}

dependencies {
    "androidRuntimeClasspath"("org.jetbrains.compose.ui:ui-tooling:${libs.versions.composeMultiplatform.get()}")
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
    alias(libs.plugins.hilt)
}

kotlin {
    androidLibrary {
        namespace = "com.example.data"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "data"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":domain"))
            implementation(project(":graphql"))
            
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.room.runtime)
            implementation(libs.room.paging)
            implementation(libs.paging.common)
            implementation(libs.datastore.preferences)
            
            // KMP Networking (Eventually replacing Retrofit)
            // implementation("io.ktor:ktor-client-core:3.0.0")
        }
        androidMain.dependencies {
            api(libs.hilt.dagger.android)
            api(libs.hilt.work)
            implementation(libs.retrofit)
            implementation(libs.converter.gson)
            implementation(libs.okhttp)
            implementation(libs.logging.interceptor)
        }
        iosMain.dependencies {
            // implementation("io.ktor:ktor-client-darwin:3.0.0")
        }
    }
}

dependencies {
    // KSP needs to be applied here for Room in KMP
    add("kspAndroid", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    
    ksp(libs.hilt.dagger.compiler)
    ksp(libs.hilt.compiler)
}

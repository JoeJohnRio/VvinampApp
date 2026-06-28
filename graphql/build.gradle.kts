plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.apollo)
}

kotlin {
    androidTarget {
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
            baseName = "graphql"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.apollo.runtime)
        }
    }
}

apollo {
    service("service") {
        packageName.set("com.example.graphql")
        mapScalarToUpload("Upload")
    }
}

android {
    namespace = "com.example.graphql"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

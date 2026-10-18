plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.serialization)
    alias(libs.plugins.compose)
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

    flavorDimensions += listOf("environment")

    productFlavors {
        create("prod") {
            dimension = "environment"
            applicationIdSuffix = ".prod"
            isDefault = true
        }
        create("mock") {
            dimension = "environment"
            applicationIdSuffix = ".mock"
        }
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinGradlePlugin.get()
    }
    sourceSets {
        getByName("main") {
            assets.srcDir(
                "${project(":composeApp").layout.buildDirectory.get().asFile}/generated/composeResourcesForAndroid/assets"
            )
        }
    }
}

kotlin {
    jvmToolchain(libs.versions.jdk.get().toInt())

    compilerOptions {
        freeCompilerArgs.addAll(listOf("-Xjvm-default=all"))
    }
}

dependencies {
    api(project(":data"))
    api(project(":composeApp"))
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.chucker.debug)
    releaseImplementation(libs.chucker.release)

    // AndroidX & Koin
    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation(libs.koin.android)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}
// Force compose resources to be copied before Android's asset merge
afterEvaluate {
    tasks.matching {
        (it.name.startsWith("merge") && it.name.contains("Assets")) ||
        it.name.startsWith("package") ||
        it.name.startsWith("bundle")
    }.configureEach {
        dependsOn(":composeApp:copyComposeResourcesToAndroidAssets")
    }
}


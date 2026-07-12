plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
//    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
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
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines.core)
            
            api(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.room.ktx)
            implementation(libs.retrofit)
            implementation(libs.converter.gson)
            implementation(libs.okhttp)
            implementation(libs.logging.interceptor)
        }
        iosMain.dependencies {
        }
    }
    afterEvaluate {
        println("=== TARGETS ===")
        kotlin.targets.forEach {
            println("${it.name} -> ${it::class.qualifiedName}")
        }

        println("=== KSP CONFIGS ===")
        configurations.names
            .filter { it.startsWith("ksp") }
            .sorted()
            .forEach(::println)
    }

//    targets.configureEach {
//        // Let Gradle use the native name ("androidLibrary" -> "kspAndroidLibrary")
//        val configurationName = "ksp${name.replaceFirstChar { it.uppercase() }}"
//        dependencies.add(configurationName, libs.room.compiler)
//    }
}
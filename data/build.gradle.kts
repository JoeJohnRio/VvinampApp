import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.serialization)
}

buildscript {
    dependencies {
        classpath(libs.buildkonfig)
    }
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
            implementation(libs.apollo.runtime)
            implementation(libs.apollo.ktor)
            
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

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
            implementation(libs.okhttp)
            implementation(libs.logging.interceptor)
            implementation(libs.chucker.debug)
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
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

    targets.configureEach {
        if (name == "metadata") return@configureEach
        val configurationName = "ksp${name.replaceFirstChar { it.uppercase() }}"
        configurations.findByName(configurationName)?.let {
            dependencies.add(it.name, libs.room.compiler)
        }
    }
}
buildkonfig {
    packageName = "com.example.data"

    // default config is required
    defaultConfigs {

        buildConfigField(
            FieldSpec.Type.STRING,
            "BASE_URL_GRAPHQL",
            providers.gradleProperty("BASE_URL_GRAPHQL").get())
        buildConfigField(
            FieldSpec.Type.STRING,
            "BASE_URL",
            providers.gradleProperty("BASE_URL").get())

    }

//    targetConfigs {
//        // names in create should be the same as target names you specified
//        create("android") {
//            buildConfigField(FieldSpec.Type.STRING, "name2", "value2")
//            buildConfigField(FieldSpec.Type.STRING, "nullableField", "NonNull-value", nullable = true)
//        }
//
//        create("ios") {
//            buildConfigField(FieldSpec.Type.STRING, "name", "valueForNative")
//        }
//    }
}
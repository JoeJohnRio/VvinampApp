pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "VvinampApp"
include(":app")
include(":data")
include(":domain")
include(":core-test")
include(":presentation")

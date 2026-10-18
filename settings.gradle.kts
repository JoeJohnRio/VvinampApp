pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "VvinampApp"
include(":data")
include(":domain")
include(":presentation")
include(":graphql")
include(":composeApp")

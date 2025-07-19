rootProject.name = "craft-engine-client-mod"
include(":fabric")
pluginManagement {
    plugins {
        kotlin("jvm") version "2.1.20"
    }
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
    }
}
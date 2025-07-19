rootProject.name = "craft-engine-client-mod"
pluginManagement {
    plugins {
        kotlin("jvm") version "2.1.20"
    }
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
    }
}
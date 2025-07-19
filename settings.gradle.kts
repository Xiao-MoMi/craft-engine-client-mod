rootProject.name = "craft-engine-client-mod"
include(":fabric")
include(":neoforge")
pluginManagement {
    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
        kotlin("jvm") version "2.1.20"
    }
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases")
    }
}
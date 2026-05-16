plugins {
    id("java")
}

subprojects {

    apply {
        plugin("java")
        plugin("java-library")
    }

    repositories {
        mavenCentral()
    }

    tasks.processResources {
        filteringCharset = "UTF-8"
    }
}

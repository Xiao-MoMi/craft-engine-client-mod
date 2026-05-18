plugins {
    id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT"
    id("com.gradleup.shadow") version "9.4.1"
}

version = property("project_version")!!
group = property("project_group")!!
val project_version: String by project
val latest_minecraft_version: String by project
val min_supported_minecraft_version: String by project

base {
    archivesName.set("craftengine")
}

tasks.shadowJar {
    relocate("org.yaml", "net.momirealms.craftengine.libraries.org.yaml")
    configurations = listOf(project.configurations.getByName("shadow"))
    from(sourceSets.main.get().output)
    val ver = if (latest_minecraft_version == min_supported_minecraft_version) latest_minecraft_version else "$min_supported_minecraft_version-$latest_minecraft_version"
    archiveFileName.set("craft-engine-fabric-mod-${project.version}+mc$ver.jar")
    destinationDirectory.set(file("$rootDir/target"))
}

loom {
    mods {
        create("craftengine") {
            sourceSet(sourceSets.main.get())
        }
    }
    accessWidenerPath = file("src/main/resources/craftengine.accesswidener")
}

repositories {
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("latest_minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    implementation("me.shedaniel.cloth:cloth-config-fabric:${property("cloth_version")}")
    implementation("com.terraformersmc:modmenu:${property("modmenu_version")}")
    shadow("org.yaml:snakeyaml:2.4")
}

tasks.processResources {
    inputs.property("version", project_version)
    inputs.property("min_minecraft_version", min_supported_minecraft_version)
    inputs.property("max_minecraft_version", latest_minecraft_version)

    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project_version,
            "min_minecraft_version" to min_supported_minecraft_version,
            "max_minecraft_version" to latest_minecraft_version,
        )
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(25)
    dependsOn(tasks.clean)
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
    withSourcesJar()
}

artifacts {
    archives(tasks.shadowJar)
}
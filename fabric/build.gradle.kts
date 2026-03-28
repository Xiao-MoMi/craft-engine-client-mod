plugins {
    id("net.fabricmc.fabric-loom-remap") version "1.14-SNAPSHOT"
    id("com.gradleup.shadow") version "9.4.1"
}

version = property("project_version")!!
group = property("project_group")!!
val project_version: String by project
val latest_minecraft_version: String by project

base {
    archivesName.set("craft-engine-fabric-mod")
}

tasks.shadowJar {
    relocate("org.yaml", "net.momirealms.craftengine.libraries.org.yaml")
    configurations = listOf(project.configurations.getByName("shadow"))
    archiveFileName.set("${base.archivesName.get()}-${project.version}-shadow.jar")
    from(sourceSets.main.get().output)
}

tasks.remapJar {
    dependsOn(tasks.shadowJar)
    inputFile.set(tasks.shadowJar.get().archiveFile)

    destinationDirectory.set(file("$rootDir/target"))
    archiveFileName.set("${base.archivesName.get()}-${project.version}+mc${rootProject.properties["latest_minecraft_version"]}.jar")
}

loom {
    mods {
        create("craft-engine-fabric-mod") {
            sourceSet(sourceSets.main.get())
        }
    }
}

repositories {
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("latest_minecraft_version")}")
    mappings(
        @Suppress("UnstableApiUsage")
        loom.layered {
            officialMojangMappings()
        }
    )
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    modApi("me.shedaniel.cloth:cloth-config-fabric:${property("cloth_version")}")
    modApi("com.terraformersmc:modmenu:${property("modmenu_version")}")
    add("shadow", "org.yaml:snakeyaml:2.4")
}

tasks.processResources {
    inputs.property("version", project_version)
    inputs.property("minecraft_version", latest_minecraft_version)

    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project_version,
            "minecraft_version" to latest_minecraft_version,
        )
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(17)
    dependsOn(tasks.clean)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
    withSourcesJar()
}

artifacts {
    archives(tasks.shadowJar)
}
plugins {
    id("net.fabricmc.fabric-loom") version "1.17-SNAPSHOT"
    id("com.gradleup.shadow") version "9.5.1"
}

version = property("project_version")!!
group = property("project_group")!!
val latestMinecraftVersion = property("latest_minecraft_version") as String
val minSupportedMinecraftVersion = property("min_supported_minecraft_version") as String

base {
    archivesName.set("craftengine")
}

tasks.shadowJar {
    relocate("org.yaml", "net.momirealms.craftengine.libraries.org.yaml")
    configurations = listOf(project.configurations.getByName("shadow"))
    from(sourceSets.main.get().output)
    val ver = if (latestMinecraftVersion == minSupportedMinecraftVersion) latestMinecraftVersion else "$minSupportedMinecraftVersion-$latestMinecraftVersion"
    archiveFileName.set("craft-engine-fabric-mod-${project.version}+mc$ver.jar")
    destinationDirectory.set(file("$rootDir/target"))
}

loom {
    mods {
        create("craftengine") {
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
    implementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    implementation("me.shedaniel.cloth:cloth-config-fabric:${property("cloth_version")}")
    implementation("com.terraformersmc:modmenu:${property("modmenu_version")}")
    shadow("org.yaml:snakeyaml:2.4")
}

tasks.processResources {
    inputs.property("version", version)
    inputs.property("min_minecraft_version", minSupportedMinecraftVersion)
    inputs.property("max_minecraft_version", latestMinecraftVersion)

    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to version,
            "min_minecraft_version" to minSupportedMinecraftVersion,
            "max_minecraft_version" to latestMinecraftVersion,
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
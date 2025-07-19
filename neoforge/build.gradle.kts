plugins {
    id("net.neoforged.moddev") version "2.0.103"
    id("com.gradleup.shadow") version "9.0.0-beta13"
}

version = property("project_version")!!
group = property("project_group")!!
val project_version: String by project
val latest_minecraft_version: String by project
val neo_version: String by project
val parchment_mappings_version: String by project
val parchment_minecraft_version: String by project
var cloth_version = property("cloth_version")

repositories {
    mavenLocal()
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
}

base {
    archivesName.set("craft-engine-neoforge-mod")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

artifacts {
    archives(tasks.shadowJar)
}

tasks.shadowJar {
    relocate("org.yaml", "net.momirealms.craftengine.libraries.org.yaml")
    configurations = listOf(project.configurations.getByName("shadow"))
    destinationDirectory.set(file("$rootDir/target"))
    archiveFileName.set("${base.archivesName.get()}-${project.version}+mc${rootProject.properties["latest_minecraft_version"]}.jar")
    from(sourceSets.main.get().output)
}

val generateModMetadata by tasks.registering(ProcessResources::class) {
    val replaceProperties = mapOf(
        "project_version" to project_version,
        "latest_minecraft_version" to latest_minecraft_version,
        "neo_version" to neo_version,
        "cloth_version" to cloth_version
    )

    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}

neoForge {
    version = neo_version
    parchment {
        mappingsVersion = parchment_mappings_version
        minecraftVersion = parchment_minecraft_version
    }

    mods {
        create("craftengine") {
            sourceSet(sourceSets.main.get())
        }
    }

    ideSyncTask(generateModMetadata)
}

sourceSets {
    main {
        resources.srcDir(generateModMetadata)
    }
}

dependencies {
    compileOnly("me.shedaniel.cloth:cloth-config-neoforge:${property("cloth_version")}")
    add("shadow", "org.yaml:snakeyaml:2.4")
}

plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

java {
    disableAutoTargetJvm()
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.commandapi)
    compileOnly(libs.configlib.yaml)
    compileOnly(libs.configlib.paper)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

group = "xyz.strealex.pluginname"
version = "0.0.1-SNAPSHOT"
description = "Plugin Description"

tasks {
    jar {
        enabled = false
    }

    shadowJar {
        archiveFileName = "${rootProject.name}-${project.version}.jar"
        archiveClassifier = null

        manifest {
            attributes["Implementation-Version"] = rootProject.version
        }
    }

    assemble {
        dependsOn(shadowJar)
    }

    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 17
    }

    withType<Javadoc>() {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        inputs.property("version", project.version)
        inputs.property("configlibVersion", libs.versions.configlib.get())

        filesMatching("plugin.yml") {
            expand(
                "version" to rootProject.version,
                "configlibVersion" to libs.versions.configlib.get()
            )
        }
    }

    defaultTasks("build")

    // 1.8.8 - 1.16.5 = Java 8
    // 1.17           = Java 16
    // 1.18 - 1.20.4  = Java 17
    // 1-20.5+        = Java 21
    val version = "1.20.4"
    val javaVersion = JavaLanguageVersion.of(17)

    val jvmArgsExternal = listOf(
        "-Dcom.mojang.eula.agree=true"
    )

    runServer {
        minecraftVersion(version)
        runDirectory = rootDir.resolve("run/paper/$version")

        javaLauncher = project.javaToolchains.launcherFor {
            languageVersion = javaVersion
        }

        downloadPlugins {
            url("https://github.com/JorelAli/CommandAPI/releases/download/9.5.1/CommandAPI-9.5.1.jar")
            url("https://github.com/EssentialsX/Essentials/releases/download/2.20.1/EssentialsX-2.20.1.jar")
            url("https://download.luckperms.net/1552/bukkit/loader/LuckPerms-Bukkit-5.4.137.jar")
            url("https://ci.lucko.me/job/spark/439/artifact/spark-bukkit/build/libs/spark-1.10.93-bukkit.jar")
        }

        jvmArgs = jvmArgsExternal
    }
}

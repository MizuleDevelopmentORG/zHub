plugins {
    id("net.kyori.indra.publishing")
    id("idea")
    id("eclipse")
    id("visual-studio")
}

val libs = extensions.getByType(org.gradle.accessors.dm.LibrariesForLibs::class)

group = rootProject.group
version = rootProject.version
description = rootProject.description

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://maven.deltapvp.net/")
}

indra {
    gpl3OnlyLicense()
    github(providers.gradleProperty("githubOrg").get(), providers.gradleProperty("githubRepo").get()) {
        ci(true)
        issues(true)
        scm(true)
    }

    configurePublications {
        pom {
            developers {
                developer {
                    id.set("powercas_gamer")
                    name.set("Cas")
                    url.set("https://deltapvp.net")
                    email.set("cas@deltapvp.net")
                    timezone.set("Europe/Amsterdam")
                }
            }
        }
    }
}

tasks {
    named("idea") {
        notCompatibleWithConfigurationCache("https://github.com/gradle/gradle/issues/13480")
    }
    register("cleanAll", Delete::class) {
        dependsOn("clean", "cleanIdea", "cleanVisualStudio", "cleanEclipse")
    }
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
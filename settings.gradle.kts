pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.deltapvp.net")
        maven(url = "https://repo.stellardrift.ca/repository/snapshots/") {
            name = "stellardriftSnapshots"
            mavenContent { snapshotsOnly() }
        }
   }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.6.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    // repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven("https://maven.deltapvp.net")
    }
}

rootProject.name = "licenser-parent"

// Make sure to update bom/build.gradle.kts when making changes to modules.

sequenceOf(
    "bom",
    "common",
    "paper",
    "api"
).forEach {
    include("licenser-$it")
    project(":licenser-$it").projectDir = file(it)
}

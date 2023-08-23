plugins {
    id("xyz.jpenilla.run-paper")
    id("zhub.common-conventions")
}

tasks {
    runServer {
        minecraftVersion(Constants.MINECRAFT_VERSION)

        jvmArguments.add("-Dcom.mojang.eula.agree=true")
        systemProperty("terminal.jline", false)
        systemProperty("terminal.ansi", true)
        jvmArguments.add("-Dnet.kyori.ansi.colorLevel=truecolor")

    }

    named("clean", Delete::class) {
       delete(project.projectDir.resolve("run"))
   }
}
plugins {
    id("zhub.paper-conventions")
}

dependencies {
    compileOnly(libs.paper)
    compileOnly(libs.annotations)
    api(libs.bstats.bukkit)
    compileOnly(libs.placeholderapi)
    compileOnly("broccolai.corn:corn-minecraft-paper:3.2.2") {
        exclude("io.papermc.paper", "paper-api")
    }
    implementation("net.kyori:adventure-text-minimessage:4.14.0")
}

applyJarMetadata("com.mizuledevelopment.zhub")

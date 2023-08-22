plugins {
    id("zhub.paper-conventions")
}

dependencies {
    compileOnly(libs.paper)
    compileOnly(libs.annotations)
    api(libs.bstats.bukkit)
    compileOnly(libs.placeholderapi)
    api(libs.configurate.yaml)
    compileOnly(libs.miniplaceholders)
    compileOnly("broccolai.corn:corn-minecraft-paper:3.2.2") {
        exclude("io.papermc.paper", "paper-api")
    }
}

applyJarMetadata("com.mizuledevelopment.zhub")

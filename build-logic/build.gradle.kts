plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(files(libs::class.java.protectionDomain.codeSource.location))
    implementation(libs.indra.common)
    implementation(libs.indra.git)
    implementation(libs.indra.spotless)
    implementation(libs.shadow)
    implementation(libs.run.task)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    target {
        compilations.configureEach {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
}
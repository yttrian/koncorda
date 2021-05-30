import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "org.yttr"
version = "0.3.0"

plugins {
    id("tanvd.kosogor") version "1.0.10" apply true
    id("io.gitlab.arturbosch.detekt") version "1.17.0" apply true
    kotlin("jvm") version "1.4.30" apply false
}

subprojects {
    apply {
        plugin("kotlin")
        plugin("tanvd.kosogor")
        plugin("io.gitlab.arturbosch.detekt")
    }

    repositories {
        mavenCentral()
        maven("https://m2.dv8tion.net/releases")
    }

    tasks.withType<KotlinCompile>() {
        kotlinOptions {
            jvmTarget = "1.8"
            languageVersion = "1.4"
            apiVersion = "1.4"
        }
    }
}

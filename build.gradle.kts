import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "org.yttr"
version = "0.1.1"

plugins {
    id("tanvd.kosogor") version "1.0.10" apply true
    id("io.gitlab.arturbosch.detekt") version "1.15.0" apply true
    kotlin("jvm") version "1.4.21" apply false
}

subprojects {
    apply {
        plugin("kotlin")
        plugin("tanvd.kosogor")
        plugin("io.gitlab.arturbosch.detekt")
    }

    repositories {
        jcenter()
    }

    tasks.withType<KotlinCompile>() {
        kotlinOptions {
            jvmTarget = "1.8"
            languageVersion = "1.4"
            apiVersion = "1.4"
        }
    }
}

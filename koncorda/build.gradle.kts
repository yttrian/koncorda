import tanvd.kosogor.proxy.publishJar

group = rootProject.group
version = rootProject.version

val jdaVersion: String by project
val hoconVersion: String by project
val coroutinesVersion: String by project
val logbackVersion: String by project

dependencies {
    implementation("net.dv8tion:JDA:$jdaVersion") {
        exclude(module = "opus-java")
    }
    api("com.typesafe:config:$hoconVersion")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutinesVersion")
    testImplementation("ch.qos.logback:logback-classic:$logbackVersion")
}

publishJar {
    bintray {
        username = "yttrian"
        repository = "koncorda"
        info {
            description = "Easy to use DSL wrapping JDA"
            githubRepo = "https://github.com/yttrian/koncorda"
            vcsUrl = githubRepo
            license = "MIT"
            labels.addAll(listOf("kotlin", "kotlin-dsl", "discord", "jda"))
        }
    }
}

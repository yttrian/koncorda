import tanvd.kosogor.proxy.publishJar

group = rootProject.group
version = rootProject.version

dependencies {
    implementation("net.dv8tion:JDA:4.2.0_227") {
        exclude(module = "opus-java")
    }
    implementation("tanvd.konfy:konfy:0.1.18")
    testImplementation("ch.qos.logback:logback-classic:1.2.3")
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

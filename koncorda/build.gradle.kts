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
    enablePublication = true
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/yttrian/koncorda")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

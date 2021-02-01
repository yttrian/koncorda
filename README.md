# Koncorda

A small DSL for quickly creating a command bot with [JDA](https://github.com/DV8FromTheWorld/JDA).
Inspired by [Ktor](https://github.com/ktorio/ktor).

# Installation

Add the JCenter repository, the latest Koncorda release, JDA, and an SLF4J implemtnation to your `build.gradle.kts`.

While Koncorda is intended to be somewhat opinionated, you are free to chose your SLF4J implementation.
Logback is recommended for the sole reason that it's what the Ktor project generator suggests.

JDA is not automatically included with Koncorda. This allows you to update to newer *non-breaking* versions of it 
without needing Koncorda to be updated as well.

```kotlin
repositories {
    jcenter()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("net.dv8tion:JDA:4.2.0_227") {
        exclude(module = "opus-java") // optional, for if you don't plan to use voice chat
    }
    implementation("org.yttr:koncorda:0.1.1")
}
```

# Getting started

Using Koncorda is easy, especially with the `koncorda` entrypoint.

```kotlin
fun main() {
    koncorda {
        commmands {
            tail("hello") {
                event.respond("Hello world!")
            }
        }
    }.start()
}
```

DSLs like `command` make adding new functionality as easy as possible. 
The above example creates a bot that responds "Hello world!" to `!hello`.

Extending the configuration is possible by implementing KoncordaConfig. By default, it defines basic needs for getting
a bot running like `DISCORD_TOKEN` and `COMMAND_PREFIX` which by default come from the environment. For more info, check
out the [konfy](https://github.com/TanVD/konfy) project.

To see what a more complex usage looks like, check out the TestBot under the tests source.

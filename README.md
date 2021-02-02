# Koncorda

![Bintray](https://img.shields.io/bintray/v/yttrian/koncorda/koncorda?label=koncorda)
![Bintray (latest)](https://img.shields.io/bintray/dt/yttrian/koncorda/koncorda)

A small DSL for quickly creating a command bot with [JDA](https://github.com/DV8FromTheWorld/JDA).
Inspired by the simplicity [Ktor](https://github.com/ktorio/ktor).

# Installation

Add the JCenter repository, the latest Koncorda release, JDA, and an SLF4J implementation to your `build.gradle.kts`.

While Koncorda is intended to be somewhat opinionated, you are free to chose your SLF4J implementation.
Logback is recommended for the sole reason that it's what the Ktor project generator suggests.

JDA is not automatically included with Koncorda. This allows you to update to newer *non-breaking* versions of it 
without needing Koncorda to be updated as well. As well as being able to chose to exclude "opus-java" if you want to.

```kotlin
repositories {
    jcenter()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("net.dv8tion:JDA:4.2.0_227") {
        exclude(module = "opus-java") // optional, for if you don't plan to use voice chat
    }
    implementation("org.yttr:koncorda:0.1.2")
}
```

# Getting started

Using Koncorda is easy, especially with the `koncorda` entrypoint.

```kotlin
fun main() {
    koncorda {
        commmands {
            tail("hello") { event.respond("Hello world!") }
        }
    }.start()
}
```

DSLs like `command` make adding new functionality as easy as possible. 
The above example creates a bot that responds "Hello world!" to `!hello`.

Extending the configuration is possible by creating an `application.conf` HOCON file. By default, Koncorda uses what
is defined in [reference.conf](koncorda/src/main/resources/reference.conf), but this can be overridden.

```hocon
koncorda {
  // The Discord bot token, comes from the environmental variable DISCORD_TOKEN
  discord-token = ${DISCORD_TOKEN}
  // The command prefix, defaults to ! or the environmental variable COMMAND_PREFIX
  command-prefix = "!"
  command-prefix = ${?COMMAND_PREFIX}
}

your-bot {
    some-option = "some value"
}
```

To see what a more complex usage of Koncorda looks like, check out the TestBot under the 
[tests source](koncorda/src/test).

# koncorda

A small DSL for quickly creating a command bot with [JDA](https://github.com/DV8FromTheWorld/JDA).
Inspired by [Ktor](https://github.com/ktorio/ktor). 

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

# Logging

While Koncorda is intended to be somewhat opinionated, you are free to chose your SLF4J implementation. 
Logback is recommended for the sole reason that it's what the Ktor project generator suggests.

```kotlin
dependecies {
    implementation("ch.qos.logback:logback-classic:1.2.3")
}
```

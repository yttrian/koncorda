# Koncorda

![GitHub release (latest by date)](https://img.shields.io/github/v/release/yttrian/koncorda)
![GitHub Sponsors](https://img.shields.io/github/sponsors/yttrian)

A small DSL for quickly creating a command bot with [JDA](https://github.com/DV8FromTheWorld/JDA).
Inspired by the simplicity [Ktor](https://github.com/ktorio/ktor).

# Installation

Add the Maven Central, Jitpack and dv8tion repositories, the latest Koncorda release, JDA, and an SLF4J implementation
to your `build.gradle.kts`.

While Koncorda is intended to be somewhat opinionated, you are free to chose your SLF4J implementation.
Logback is recommended for the sole reason that it's what the Ktor project generator suggests.

JDA is not automatically included with Koncorda. This allows you to update to newer *non-breaking* versions of it 
without needing Koncorda to be updated as well. As well as being able to chose to exclude "opus-java" if you want to.

```kotlin
repositories {
    mavenCentral()
    maven("https://m2.dv8tion.net/releases")
    maven("https://jitpack.io")
}
```
```kotlin
dependencies {
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("net.dv8tion:JDA:4.2.1_253") {
        exclude(module = "opus-java") // optional, for if you don't plan to use voice chat
    }
    implementation("com.github.yttrian:koncorda:0.3.0")
}
```

# Getting started

Using Koncorda is easy, especially with the `koncorda` entrypoint.

```kotlin
fun main() {
    koncorda {
        commmands {
            leaf("hello") { event.respond("Hello world!") }
        }
    }.start()
}
```

DSLs like `commands` make adding new functionality as easy as possible. 
The above example creates a bot that responds "Hello world!" to `!hello`.

Extending the configuration is possible by creating an `application.conf` HOCON file. By default, Koncorda uses what
is defined in [reference.conf](koncorda/src/main/resources/reference.conf), but this can be overridden.

```hocon
koncorda {
  command-prefix = "/"
}

your-bot {
  some-option = "some value"
}
```

To see what a more complex usage of Koncorda looks like, check out the TestBot under the 
[tests source](koncorda/src/test).

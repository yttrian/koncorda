import command.HelpCommand
import command.TestCommand
import command.check.Useless
import net.dv8tion.jda.api.requests.GatewayIntent
import org.yttr.koncorda.await
import org.yttr.koncorda.command.check.Impossible
import org.yttr.koncorda.commands
import org.yttr.koncorda.koncorda
import org.yttr.koncorda.onReady

fun main() {
    koncorda {
        gatewayIntents = listOf(GatewayIntent.GUILD_MESSAGES)

        commands {
            leaf("help", HelpCommand)
            branch("test") {
                check(Useless) {
                    branch("a") {
                        check(Impossible) {
                            leaf("b", TestCommand("You reached b!"))
                        }
                    }
                }
                branch("c", TestCommand("You reached c, try c d!")) {
                    leaf("d", TestCommand("You reached d!"))
                }
            }
            leaf("hello") {
                event.respond("Hello ${event.author}!")
            }
            check(Useless) {
                leaf("world") { event.respond("Yay!") }
            }
        }

        onReady {
            val ping = it.jda.restPing.await()
            logger.debug("Ping: $ping")
        }
    }.start(lowMemory = true)
}

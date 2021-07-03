import command.HelpCommand
import command.TestCommand
import command.check.Useless
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import org.yttr.koncorda.await
import org.yttr.koncorda.command.check.Impossible
import org.yttr.koncorda.command.check.and
import org.yttr.koncorda.command.check.not
import org.yttr.koncorda.command.check.or
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
                leaf("world") { event.respond(MessageBuilder().setContent("Yay!").build()) }
            }
            check(Useless and Impossible) {
                leaf("everyone") { event.respond("Huh?") }
            }
            check(Useless and !Impossible) {
                leaf("everybody") { event.respond("Hmm.") }
            }
            check(Useless or Impossible) {
                leaf("everything") { event.respond("Hmm.") }
            }
        }

        onReady {
            val ping = it.jda.restPing.await()
            logger.debug("Ping: $ping")
        }
    }.start(lowMemory = true)
}

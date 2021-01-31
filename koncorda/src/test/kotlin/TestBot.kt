import command.HelpCommand
import command.TestCommand
import org.yttr.koncorda.command.check.Impossible
import org.yttr.koncorda.commands
import org.yttr.koncorda.koncorda

fun main() {
    koncorda(TestBotConfig) {
        commands {
            leaf("help", HelpCommand)
            branch("test") {
                check(command.check.Useless) {
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
        }
    }.start(lowMemory = true)
}

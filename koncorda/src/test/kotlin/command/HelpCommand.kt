package command

import org.yttr.koncorda.command.CommandCall
import org.yttr.koncorda.command.CommandHandler

/**
 * A help command explaining how to use the bot.
 */
object HelpCommand : CommandHandler {
    override fun CommandCall.handle() {
        event.respond("Hello world!")
    }
}

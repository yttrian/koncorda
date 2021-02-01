package command

import org.yttr.koncorda.command.CommandCall
import org.yttr.koncorda.command.CommandHandler

class TestCommand(private val content: String) : CommandHandler {
    override fun CommandCall.handle() {
        event.respond(content)
        event.respond(args.joinToString(" "))
    }
}

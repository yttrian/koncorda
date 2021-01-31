package command.check

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.yttr.koncorda.command.check.CommandCheck

object Useless : CommandCheck {
    override fun check(event: MessageReceivedEvent): Boolean = true
}

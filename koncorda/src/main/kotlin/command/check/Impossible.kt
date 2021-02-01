package org.yttr.koncorda.command.check

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

object Impossible : CommandCheck {
    override fun check(event: MessageReceivedEvent): Boolean = false
}

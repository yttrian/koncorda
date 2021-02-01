package org.yttr.koncorda.command.check

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

interface CommandCheck {
    fun check(event: MessageReceivedEvent): Boolean
}

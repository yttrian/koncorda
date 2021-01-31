package org.yttr.koncorda.command

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

data class CommandCall(val event: MessageReceivedEvent, val args: List<String>) {
    /**
     * Respond to the event.
     */
    fun MessageReceivedEvent.respond(content: String) {
        if (content.isNotBlank()) channel.sendMessage(content).queue()
    }
}

package org.yttr.koncorda.command

import kotlinx.coroutines.future.await
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

data class CommandCall(val event: MessageReceivedEvent, val args: List<String>) {
    /**
     * Respond to the event.
     */
    suspend fun MessageReceivedEvent.respond(content: String) {
        if (content.isNotBlank()) channel.sendMessage(content).submit().await()
    }
}

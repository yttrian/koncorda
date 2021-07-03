package org.yttr.koncorda.command

import kotlinx.coroutines.future.await
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

data class CommandCall(val event: MessageReceivedEvent, val args: List<String>) {
    /**
     * Respond to the event with text.
     */
    suspend fun MessageReceivedEvent.respond(content: String) {
        if (content.isNotBlank()) respond(MessageBuilder().setContent(content).build())
    }

    /**
     * Respond to the event with an embed.
     */
    suspend fun MessageReceivedEvent.respond(embed: MessageEmbed) {
        respond(MessageBuilder().setEmbed(embed).build())
    }

    /**
     * Respond the the event with a complex message.
     */
    suspend fun MessageReceivedEvent.respond(message: Message) {
        channel.sendMessage(message).submit().await()
    }
}

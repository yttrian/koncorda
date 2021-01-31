package org.yttr.koncorda

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.yttr.koncorda.command.Command
import org.yttr.koncorda.config.DefaultKoncordaConfig
import org.yttr.koncorda.config.KoncordaConfig
import tanvd.konfy.ConfigView

/**
 * The Discord bot application, with useful DSLs for setting up different features.
 */
class Koncorda(val config: KoncordaConfig) : ConfigView, ListenerAdapter() {
    private val baseCommands = mutableListOf<Command.Branch>()

    /**
     * Complete the JDA builder and start the bot
     * @param lowMemory Whether or not to use JDA's low memory profile
     */
    fun start(lowMemory: Boolean = false) = if (lowMemory) {
        JDABuilder.createLight(config.discordToken)
    } else {
        JDABuilder.createDefault(config.discordToken)
    }.addEventListeners(this).setEnabledIntents(config.gatewayIntents).build()

    internal fun addBaseCommand(command: Command.Branch) = baseCommands.add(command)

    override fun onMessageReceived(event: MessageReceivedEvent) {
        // ignore improper messages
        if (event.isIgnorable) return

        // split the args
        val args = event.message.contentStripped.trim().split(" ")

        // find the command handler, if it exists
        when (val baseCommand = allBaseCommands()[args.first()]) {
            is Command.Leaf -> baseCommand
            is Command.Branch -> baseCommand[args.drop(1)]
            else -> null
        }?.let {
            if (it.check(event)) it(event, args) else {
                event.channel.sendMessage("You are not allowed to use that command.").queue()
            }
        }
    }

    private fun allBaseCommands(): Map<String, Command> = baseCommands
        .map { it.routes }
        .fold(emptyMap()) { acc, baseRoute ->
            acc + baseRoute
        }

    private val MessageReceivedEvent.isIgnorable
        get() = author.isBot || isWebhookMessage
}

/**
 * Commands DSL, an easy way of registering command handlers and the path to them with Koncorda.
 */
fun Koncorda.commands(prefix: String = config.commandPrefix, build: Command.Branch.() -> Unit) =
    addBaseCommand(Command.Branch(prefix = prefix).apply(build))

/**
 * The simplest way to start building a bot with Koncorda. Make sure to add .start() to the end!
 */
fun koncorda(config: KoncordaConfig = DefaultKoncordaConfig, init: Koncorda.() -> Unit) =
    Koncorda(config).apply(init)

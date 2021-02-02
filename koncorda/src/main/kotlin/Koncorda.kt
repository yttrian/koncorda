package org.yttr.koncorda

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import org.yttr.koncorda.command.Command

/**
 * The Discord bot application, with useful DSLs for setting up different features.
 */
class Koncorda : ListenerAdapter() {
    private val discordToken = conf.getString("koncorda.discord-token")
    private val baseCommands = mutableListOf<Command.Branch>()

    /**
     * Explicitly listed gateways intents, defaults to JDA defaults
     */
    var gatewayIntents = GatewayIntent.getIntents(GatewayIntent.DEFAULT).toList()

    /**
     * Complete the JDA builder and start the bot
     * @param lowMemory Whether or not to use JDA's low memory profile
     */
    fun start(lowMemory: Boolean = false) = if (lowMemory) {
        JDABuilder.createLight(discordToken)
    } else {
        JDABuilder.createDefault(discordToken)
    }.addEventListeners(this).setEnabledIntents(gatewayIntents).build()

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

    companion object {
        /**
         * Access to configuration defined in application.conf HOCON
         */
        val conf: Config = ConfigFactory.load()
    }
}

/**
 * Commands DSL, an easy way of registering command handlers and the path to them with Koncorda.
 */
fun Koncorda.commands(
    prefix: String = Koncorda.conf.getString("koncorda.command-prefix"),
    build: Command.Branch.() -> Unit
) = addBaseCommand(Command.Branch(prefix = prefix).apply(build))

/**
 * The simplest way to start building a bot with Koncorda. Make sure to add .start() to the end!
 */
fun koncorda(init: Koncorda.() -> Unit) = Koncorda().apply(init)

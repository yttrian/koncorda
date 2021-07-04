package org.yttr.koncorda

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.Event
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.requests.GatewayIntent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.yttr.koncorda.command.Command

/**
 * The Discord bot application, with useful DSLs for setting up different features.
 */
class Koncorda {
    val logger: Logger = LoggerFactory.getLogger(this::class.simpleName)
    private val discordToken = conf.getString("koncorda.discord-token")
    internal val baseCommands = mutableListOf<Command.Branch>()
    internal val eventListeners = mutableListOf<EventListener>()
    internal val configs = mutableListOf<(BuilderConfig) -> Unit>()

    private val job = SupervisorJob()
    internal val scope = CoroutineScope(Dispatchers.Default + job)

    /**
     * Explicitly listed gateways intents, defaults to JDA defaults
     */
    var gatewayIntents: List<GatewayIntent> = GatewayIntent.getIntents(GatewayIntent.DEFAULT).toList()

    /**
     * Complete the JDA builder and start the bot
     * @param lowMemory Whether or not to use JDA's low memory profile
     */
    fun start(lowMemory: Boolean = false) = if (lowMemory) {
        JDABuilder.createLight(discordToken)
    } else {
        JDABuilder.createDefault(discordToken)
    }.also { builder ->
        eventListeners.forEach { builder.addEventListeners(it) }
        configs.forEach { config ->
            with (config) {
                config(builder)
            }
        }
    }.build()

    internal inline fun <reified E : Event> addEventListener(crossinline action: suspend (E) -> Unit) {
        val listener = EventListener {
            if (it is E && !(it is MessageReceivedEvent && it.isIgnorable)) {
                scope.launch { action(it) }
            }
        }
        eventListeners.add(listener)
    }

    init {
        addEventListener<MessageReceivedEvent> { event ->
            // split the args
            val args = event.message.contentStripped.trim().split(" ")

            // find the command handler, if it exists
            when (val baseCommand = allBaseCommands()[args.first()]) {
                is Command.Leaf -> baseCommand
                is Command.Branch -> baseCommand[args.drop(1)]
                else -> null
            }?.let {
                val call = it.createCall(event, args)
                if (it.check(call)) it.handle(call) else {
                    event.channel.sendMessage("You are not allowed to use that command.").queue()
                }
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
) = baseCommands.add(Command.Branch(prefix = prefix).apply(build))

/**
 * Function to run when the bot enters the ready state after being started.
 */
fun Koncorda.onReady(action: suspend (ReadyEvent) -> Unit) = addEventListener(action)

/**
 *
 */
fun Koncorda.configure(config: (BuilderConfig) -> Unit) = configs.add(config)

/**
 * The simplest way to start building a bot with Koncorda. Make sure to add .start() to the end!
 */
inline fun koncorda(init: Koncorda.() -> Unit) = Koncorda().apply(init)

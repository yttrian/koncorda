package org.yttr.koncorda.config

import net.dv8tion.jda.api.requests.GatewayIntent
import tanvd.konfy.ConfigChain
import tanvd.konfy.ConfigView
import tanvd.konfy.provided
import tanvd.konfy.provider.ConfigProvider
import tanvd.konfy.provider.EnvVarProvider

/**
 * Defines all needed configuration properties
 */
abstract class KoncordaConfig(private val chain: ConfigChain = ConfigChain(EnvVarProvider())) : ConfigView {
    override val provider: ConfigProvider
        get() = chain

    /**
     * The Discord bot token
     */
    val discordToken: String by provided("DISCORD_TOKEN")

    /**
     * Explicitly listed gateways intents, defaults to JDA defaults
     */
    open val gatewayIntents: List<GatewayIntent> = GatewayIntent.getIntents(GatewayIntent.DEFAULT).toList()

    /**
     * The prefix for commands like !help if !
     */
    open val commandPrefix: String by provided("COMMAND_PREFIX", "!")
}

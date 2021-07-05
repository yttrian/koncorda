package org.yttr.koncorda.configure

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag

class BuilderMemoryConfig : Modify {
    private val enabledIntents = GatewayIntent.getIntents(GatewayIntent.DEFAULT).toMutableSet()
    private val enabledCaches = mutableSetOf<CacheFlag>()

    fun clearEnabledIntents() = enabledIntents.clear()
    fun clearEnabledCaches() = enabledCaches.clear()

    /**
     * Enable intent
     */
    operator fun GatewayIntent.unaryPlus() = enabledIntents.add(this)

    /**
     * Disable intent
     */
    operator fun GatewayIntent.unaryMinus() = enabledIntents.remove(this)

    /**
     * Enable cache AND relevant gateway intents
     */
    operator fun CacheFlag.unaryPlus() {
        enabledCaches.add(this)
        when (this) {
            CacheFlag.ACTIVITY -> enabledIntents.add(GatewayIntent.GUILD_PRESENCES)
            CacheFlag.VOICE_STATE -> enabledIntents.add(GatewayIntent.GUILD_VOICE_STATES)
            CacheFlag.EMOTE -> enabledIntents.add(GatewayIntent.GUILD_EMOJIS)
            CacheFlag.CLIENT_STATUS -> enabledIntents.add(GatewayIntent.GUILD_PRESENCES)
            else -> Unit
        }
    }

    /**
     * Disable cache
     */
    operator fun CacheFlag.unaryMinus() = enabledCaches.remove(this)

    override fun JDABuilder.configure() {
        setEnabledIntents(enabledIntents)
        disableCache(CacheFlag.values().asList())
        enableCache(enabledCaches)
    }
}

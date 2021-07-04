package org.yttr.koncorda

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag

class BuilderConfig {
    private val intents = GatewayIntent.getIntents(GatewayIntent.DEFAULT).toMutableSet()
    private val cacheFlagActions = mutableListOf<(JDABuilder) -> Unit>()

    fun clearIntents() = intents.clear()

    operator fun GatewayIntent.unaryPlus() = intents.add(this)
    operator fun GatewayIntent.unaryMinus() = intents.add(this)

    operator fun CacheFlag.unaryPlus() = cacheFlagActions.add { it.enableCache(this) }
    operator fun CacheFlag.unaryMinus() = cacheFlagActions.add { it.disableCache(this) }

    fun JDABuilder.configure() {
        setEnabledIntents(intents)
        cacheFlagActions.forEach { it(this) }
    }
}

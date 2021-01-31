import net.dv8tion.jda.api.requests.GatewayIntent
import org.yttr.koncorda.config.KoncordaConfig

object TestBotConfig : KoncordaConfig() {
    override val gatewayIntents: List<GatewayIntent>
        get() = listOf(GatewayIntent.GUILD_MESSAGES)
}

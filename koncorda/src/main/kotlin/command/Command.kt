package org.yttr.koncorda.command

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.yttr.koncorda.CreationDsl
import org.yttr.koncorda.command.check.CommandCheck

/**
 * A callable bot command.
 */
sealed class Command(
    protected val depth: Int,
    private val prefix: String = ""
) {
    protected val checks: MutableSet<CommandCheck> = mutableSetOf()
    protected val nextDepth = depth + 1

    /**
     * Represents a command branch.
     * @param handler action to perform when branch is called a terminal, defaults to autogenerated help
     * @param prefix the command prefix, only used by the base route!
     */
    class Branch(depth: Int = 0, handler: CommandHandler? = null, prefix: String = "") : Command(depth, prefix) {
        val routes = mutableMapOf<String, Command>()
        private val fallback by lazy {
            if (handler != null) Leaf(depth, handler) else {
                val autoHelpCommandHandler = object : CommandHandler {
                    override suspend fun CommandCall.handle() {
                        val path = args.joinToString(" ")
                        val validSubcommands = routes.keys.joinToString { "`$it`" }
                        event.respond("Valid subcommands for `$path` are $validSubcommands.")
                    }
                }
                Leaf(depth, autoHelpCommandHandler, true).also { it.checks.addAll(checks) }
            }
        }

        /**
         * Try to follow a path to a terminal.
         * Cannot tailrec optimize: https://stackoverflow.com/a/44626117
         */
        operator fun get(path: List<String>): Leaf {
            val arg = path.firstOrNull() ?: return fallback

            return when (val foundCommand = routes[arg]) {
                is Branch -> foundCommand[path.drop(1)]
                is Leaf -> foundCommand
                else -> fallback
            }
        }

        override fun addCheck(addedCheck: CommandCheck) {
            this.checks += addedCheck
            this.routes.values.forEach { it.addCheck(addedCheck) }
        }
    }

    /**
     * Represents the end of a branch where a command will be handled.
     */
    class Leaf(
        depth: Int,
        private val handler: CommandHandler,
        private val help: Boolean = false
    ) : Command(depth) {
        fun createCall(event: MessageReceivedEvent, args: List<String>) =
            CommandCall(event, if (help) args.take(depth) else args.drop(depth))

        suspend fun handle(call: CommandCall) = with(handler) {
            call.handle()
        }

        override fun addCheck(addedCheck: CommandCheck) {
            this.checks += addedCheck
        }
    }

    /**
     * Build a branch.
     */
    @CreationDsl
    fun Branch.branch(arg: String, handler: CommandHandler? = null, build: Branch.() -> Unit) {
        routes["$prefix$arg"] = Branch(nextDepth, handler).apply(build)
    }

    /**
     * Set the handler.
     */
    @CreationDsl
    fun Branch.leaf(arg: String, handler: CommandHandler) {
        routes["$prefix$arg"] = Leaf(nextDepth, handler)
    }

    /**
     * Set the handler.
     */
    @CreationDsl
    fun Branch.leaf(arg: String, handler: suspend CommandCall.() -> Unit) {
        routes["$prefix$arg"] = Leaf(nextDepth, object : CommandHandler {
            override suspend fun CommandCall.handle() = handler()
        })
    }

    /**
     * Create a checked branch.
     */
    fun Branch.check(check: CommandCheck, build: Branch.() -> Unit) {
        val checkedBranch = Branch().also(build)
        checkedBranch.addCheck(check)
        this.routes += checkedBranch.routes
    }

    abstract fun addCheck(addedCheck: CommandCheck)

    suspend fun check(call: CommandCall) = checks.all { it.check(call) }
}

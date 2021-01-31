package org.yttr.koncorda.command

/**
 * A command handler.
 */
interface CommandHandler {
    /**
     * Handle the message event for a command invocation.
     */
    fun CommandCall.handle()
}

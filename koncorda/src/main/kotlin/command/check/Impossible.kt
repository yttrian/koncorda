package org.yttr.koncorda.command.check

import org.yttr.koncorda.command.CommandCall

object Impossible : CommandCheck {
    override suspend fun check(call: CommandCall): Boolean = false
}

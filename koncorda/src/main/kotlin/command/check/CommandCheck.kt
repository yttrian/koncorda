package org.yttr.koncorda.command.check

import org.yttr.koncorda.command.CommandCall

interface CommandCheck {
    suspend fun check(call: CommandCall): Boolean
}

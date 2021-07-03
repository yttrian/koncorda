package org.yttr.koncorda.command.check

import org.yttr.koncorda.command.CommandCall

interface CommandCheck {
    suspend fun check(call: CommandCall): Boolean
}

infix fun CommandCheck.or(right: CommandCheck): CommandCheck = composite(this, right, Boolean::or)

infix fun CommandCheck.and(right: CommandCheck): CommandCheck = composite(this, right, Boolean::and)

operator fun CommandCheck.not(): CommandCheck = object : CommandCheck {
    override suspend fun check(call: CommandCall): Boolean = !this@not.check(call)
}

private fun composite(
    left: CommandCheck,
    right: CommandCheck,
    predicate: (Boolean, Boolean) -> Boolean
): CommandCheck {
    return object : CommandCheck {
        override suspend fun check(call: CommandCall): Boolean {
            return predicate(left.check(call), right.check(call))
        }
    }
}
